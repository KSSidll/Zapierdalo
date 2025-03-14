package com.kssidll.zapierdalo.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import co.anbora.labs.spatia.geometry.Point
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.DeviceOrientation
import com.google.android.gms.location.DeviceOrientationListener
import com.google.android.gms.location.DeviceOrientationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.FusedOrientationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.kssidll.zapierdalo.APPLICATION_NAME
import com.kssidll.zapierdalo.MainActivity
import com.kssidll.zapierdalo.R
import com.kssidll.zapierdalo.broadcast.RunningActionServiceStopActionReceiver
import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.domain.usecase.gps.InsertGpsEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.InsertRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.SetRunActionEndTimestampUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.GetLastStepsEntityForRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.InsertStepsEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.SetStepsCountUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.SetStepsEndTimestampUseCase
import com.kssidll.zapierdalo.helper.checkPermission
import com.kssidll.zapierdalo.helper.getLocalizedString
import com.kssidll.zapierdalo.service.RunningActionService.Companion.UNDEFINED_RUN_ACTION_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Possible actions that the [RunningActionService] can perform
 */
enum class RunningActionServiceActions {
    START,
    STOP
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("ComposeNamingUppercase", "ComposableNaming")
@Composable
@Stable
fun rememberRunningActionServicePreparationLauncher(
    onSuccess: () -> Unit,
    onFailure: () -> Unit
): MultiplePermissionsState {
    val context = LocalContext.current

    val requestLocationSettings =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            onSuccess()
        }

    val requestLocationSetting: () -> Unit = {
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(RunningActionService.locationRequest)
            .build()

        val client = LocationServices.getSettingsClient(context)
        val settingsTask = client.checkLocationSettings(settingsRequest)

        settingsTask.addOnSuccessListener {
            onSuccess()
        }

        settingsTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution)
                        .build()

                    requestLocationSettings.launch(intentSenderRequest)
                } catch (_: IntentSender.SendIntentException) {
                    // Ignore
                }
            }
        }
    }

    return rememberMultiplePermissionsState(
        permissions = RunningActionService.Permissions.ALL.asList(),
        onPermissionsResult = { permissionResultMap ->
            val locationPermissions = RunningActionService.Permissions.LOCATION.all {
                it in permissionResultMap.keys && permissionResultMap[it] == true
            }

            val activityPermissions = RunningActionService.Permissions.ACTIVITY.all {
                it in permissionResultMap.keys && permissionResultMap[it] == true
            }

            if (locationPermissions) {
                requestLocationSetting()
            } else if (activityPermissions) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    )
}

@AndroidEntryPoint
class RunningActionService: Service(), SensorEventListener {

    private lateinit var serviceJob: Job
    private lateinit var serviceScope: CoroutineScope

    private val locationThread = HandlerThread(LOCATION_THREAD_NAME, Process.THREAD_PRIORITY_MORE_FAVORABLE)

    private var sensorManager: SensorManager? = null

    @Inject
    lateinit var insertGpsEntityUseCase: InsertGpsEntityUseCase

    @Inject
    lateinit var insertRunActionEntityUseCase: InsertRunActionEntityUseCase

    @Inject
    lateinit var getRunActionEntityUseCase: GetRunActionUseCase

    @Inject
    lateinit var setRunActionEndTimestampUseCase: SetRunActionEndTimestampUseCase

    @Inject
    lateinit var getLastStepsEntityForRunActionUseCase: GetLastStepsEntityForRunActionUseCase

    @Inject
    lateinit var insertStepsEntityUseCase: InsertStepsEntityUseCase

    @Inject
    lateinit var setStepsEndTimestampUseCase: SetStepsEndTimestampUseCase

    @Inject
    lateinit var setStepsCountUseCase: SetStepsCountUseCase

    private var activeRunActionIdList: MutableList<Long> = mutableListOf()
    private var stepsStartCount: Long? = null

    // gps sensor
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null

    // step counter sensor
    private var stepCounterSensor: Sensor? = null

    private fun registerLocaleChangeReceiver() {
        val filter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        registerReceiver(localeChangeReceiver, filter)
    }

    private val localeChangeReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_LOCALE_CHANGED) {
                updateNotificationChannel()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        Log.d(TAG, "onStartCommand: Executed with startId: $startId")

        if (intent != null) {
            val runActionId = intent.getLongExtra(RUN_ACTION_KEY, UNDEFINED_RUN_ACTION_ID)

            when (intent.action) {
                RunningActionServiceActions.START.name -> startAction(runActionId)
                RunningActionServiceActions.STOP.name -> stopAction(runActionId)
                else -> Log.e(TAG, "onStartCommand: No action in the received intent")
            }
        } else {
            Log.e(TAG, "onStartCommand: No intent")
        }

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate: Service created")

        // Initialize the scope
        serviceJob = Job()
        serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

        // Initialize receivers
        registerLocaleChangeReceiver()

        // Initialize sensors
        sensorManager = ContextCompat.getSystemService(
            applicationContext,
            SensorManager::class.java
        )
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    override fun onDestroy() {
        locationCallback?.let {
            Log.d(TAG, "onDestroy: Removing location updates")
            locationThread.quitSafely()
            fusedLocationClient.removeLocationUpdates(it)
        }

        Log.d(TAG, "onDestroy: Unregistering sensor listeners")
        unregisterReceiver(localeChangeReceiver)
        sensorManager?.unregisterListener(this)

        Log.d(TAG, "onDestroy: Canceling the service job")
        serviceJob.cancel()

        super.onDestroy()
        Log.d(TAG, "onDestroy: Service destroyed")
    }

    /**
     * Initializes the service
     * Creates the notification channel
     * Starts the foreground notification
     */
    private fun init() {
        createNotificationChannel()

        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createNotification(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            } else {
                0
            }
        )
    }

    /**
     * Activates the specified [runActionId]
     */
    private fun activateRunAction(runActionId: Long) {
        if (runActionId == UNDEFINED_RUN_ACTION_ID) {
            Log.e(TAG, "activateRunAction: Attempted to activate undefined run action id")
            return
        }

        if (runActionId !in activeRunActionIdList) {
            Log.d(TAG, "activateRunAction: Activating $runActionId")

            activeRunActionIdList.add(runActionId)
        } else {
            Log.d(TAG, "activateRunAction: Tried to activate $runActionId, but it's already active")
        }
    }

    /**
     * Deactivates specified [runActionId] and updates affected entities
     * [UNDEFINED_RUN_ACTION_ID] deactivates all active run actions
     */
    private fun deactivateRunAction(runActionId: Long): Job {
        return serviceScope.launch {
            val endTimestamp = Calendar.getInstance().timeInMillis

            if (runActionId == UNDEFINED_RUN_ACTION_ID) {
                Log.d(TAG, "stopAction: Deactivating all run actions")

                // TODO refactor steps entity handling and update here
                activeRunActionIdList.forEach { entityId ->
                    getLastStepsEntityForRunActionUseCase(entityId)?.let { lastStepsEntity ->
                        setStepsEndTimestampUseCase(lastStepsEntity.id, endTimestamp)
                    }
                    setRunActionEndTimestampUseCase(entityId, endTimestamp)
                }

                activeRunActionIdList.clear()
            } else {
                val wasDeactivated = activeRunActionIdList.remove(runActionId)

                if (wasDeactivated) {
                    Log.d(TAG, "stopAction: Deactivating run action $runActionId")

                    getLastStepsEntityForRunActionUseCase(runActionId)?.let { lastStepsEntity ->
                        setStepsEndTimestampUseCase(lastStepsEntity.id, endTimestamp)
                    }
                    setRunActionEndTimestampUseCase(runActionId, endTimestamp)
                } else {
                    Log.d(TAG, "stopAction: Tried to deactivate run action $runActionId, but it's not active")
                }
            }
        }
    }

    /**
     * Starts service for the specified [runActionId]
     */
    private fun startAction(runActionId: Long) {
        if (runActionId == UNDEFINED_RUN_ACTION_ID) {
            Log.e(TAG, "startAction: Attempted to start for undefined run action id")
            return
        }

        activateRunAction(runActionId)

        Log.d(TAG, "startAction: Attempting to start")

        // early return if already started
        if (getServiceState(SERVICE_NAME) == ServiceState.STARTED) {
            Log.d(TAG, "startAction: Service already set as running")
            return
        }

        // set as started
        setServiceState(SERVICE_NAME, ServiceState.STARTED)

        Log.d(TAG, "startAction: Started")

        init()

        if (checkPermission(applicationContext, Permissions.ACTIVITY.asList())) {
            stepCounterSensor?.let { stepCounterSensor ->
                sensorManager?.registerListener(
                    this,
                    stepCounterSensor,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
        }

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    Log.d(TAG, "locationCallback: received location")

                    serviceScope.launch {
                        // we don't batch as having many active run actions is not expected
                        activeRunActionIdList.forEach { entityId ->
                            val entity = GpsEntity(
                                runActionId = entityId,
                                location = Point(location.latitude, location.longitude),
                                accuracy = location.accuracy,
                                speed = location.speed * 3.6f, // parse to kmh
                            )

                            insertGpsEntityUseCase(entity)
                        }
                    }
                }
            }
        }

        if (checkPermission(applicationContext, Permissions.LOCATION.asList())) {
            locationCallback?.let { callback ->
                try { // ignore exception if thread somehow starts more than once
                    locationThread.start()
                } catch (_: IllegalThreadStateException) {}

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    callback,
                    locationThread.looper
                )
            }
        }
    }

    /**
     * Deactivates specified [runActionId] and stops the service if none are active
     * @param runActionId id of the run action to deactivate, [UNDEFINED_RUN_ACTION_ID] to stop all
     */
    private fun stopAction(runActionId: Long) {
        deactivateRunAction(runActionId).invokeOnCompletion {
            if (activeRunActionIdList.isEmpty()) {
                Log.d(TAG, "stopAction: No active run actions")

                stop()
            }
        }
    }

    /**
     * Unsubscribes from the sensor updates and stops the service
     */
    private fun stop() {
        Log.d(TAG, "stop: Stopping the service")

        setServiceState(SERVICE_NAME, ServiceState.STOPPED)

        try {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)

            stopSelf()
        } catch (e: Exception) {
            Log.d(TAG, "stop: Service stopped without being started: ${e.message}")
        }
    }

    /**
     * Creates the notification channel for the service
     *
     * Has to be called before any notifications can be posted
     */
    private fun createNotificationChannel() {
        Log.d(TAG, "createNotificationChannel: creating")

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        notificationManager.createNotificationChannel(makeNotificationChannel())
    }

    /**
     * Updates the notification channel for the service
     *
     * Changes the name and the description to current locale
     */
    private fun updateNotificationChannel() {
        Log.d(TAG, "updateNotificationChannel: updating")

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        notificationManager.createNotificationChannel(makeNotificationChannel())
    }

    /**
     * @return notification channel for the service
     */
    private fun makeNotificationChannel(): NotificationChannelCompat {
        Log.d(TAG, "makeNotificationChannel: making")

        return NotificationChannelCompat.Builder(
            NOTIFICATION_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_LOW
        )
            .setName(getLocalizedString(R.string.service_running_action_channel_name))
            .setDescription(getLocalizedString(R.string.service_running_action_channel_description))
            .setLightsEnabled(false)
            .setVibrationEnabled(false)
            .build()
    }

    /**
     * Creates the initial notification for the service
     *
     * [createNotificationChannel] has to be called before any notifications can be posted
     * @return the initial notification
     */
    private fun createNotification(): Notification {
        Log.d(TAG, "createNotification: creating")

        val pendingIntent: PendingIntent = Intent(
            this,
            MainActivity::class.java
        ).let { notificationIntent ->
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(
            applicationContext,
            NOTIFICATION_CHANNEL_ID
        )

        // Stop data export action button
        val stopServiceIntent = Intent(
            this,
            RunningActionServiceStopActionReceiver::class.java
        )

        val stopServicePendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            stopServiceIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopServiceAction = NotificationCompat.Action.Builder(
            R.drawable.stop,
            getLocalizedString(R.string.service_running_action_stop),
            stopServicePendingIntent
        )
            .build()

        return builder
            .setContentTitle(APPLICATION_NAME)
            .setContentText("Running") // TODO make
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .addAction(stopServiceAction)
            .build()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        // location defined in start
        sensorEvent?.let { event ->
            when (event.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    Log.d(TAG, "stepsCallback: receiving data")

                    val steps = event.values.lastOrNull()?.toLong() ?: return

                    if (stepsStartCount == null) {
                        stepsStartCount = steps
                    }

                    val additionalSteps = steps - stepsStartCount!!
                    stepsStartCount = steps

                    Log.d(TAG, "stepsCallback: received $steps, incremented by $additionalSteps")

                    serviceScope.launch {
                        // we don't batch as having many active run actions is not expected
                        activeRunActionIdList.forEach { entityId ->
                            @Suppress("LocalVariableName")
                            val _tmp_lastSteps = getLastStepsEntityForRunActionUseCase(entityId)

                            if (_tmp_lastSteps == null) {
                                Log.d(
                                    TAG,
                                    "stepsCallback: inserting new steps entity for run action $entityId, no previous entity"
                                )
                                insertStepsEntityUseCase(StepsEntity(entityId))
                            } else if (_tmp_lastSteps.endTimestamp != null) {
                                Log.d(
                                    TAG,
                                    "stepsCallback: inserting new steps entity for run action $entityId, previous entity marked as finished"
                                )
                                insertStepsEntityUseCase(StepsEntity(entityId))
                            }

                            val lastSteps = getLastStepsEntityForRunActionUseCase(entityId)
                            if (lastSteps == null) {
                                Log.e(TAG, "stepsCallback: last steps for run action $entityId is null")
                            } else if (lastSteps.endTimestamp != null) {
                                Log.e(
                                    TAG,
                                    "stepsCallback: last steps for run action $entityId is marked as finished but is being updated"
                                )
                            }

                            lastSteps?.let {
                                setStepsCountUseCase(it.id, it.steps + additionalSteps)
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    object Permissions {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS

        private const val LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION

        @RequiresApi(Build.VERSION_CODES.Q)
        private const val ACTIVITY_RECOGNITION = Manifest.permission.ACTIVITY_RECOGNITION

        val ALL = buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(NOTIFICATIONS)
            }
            add(LOCATION_COARSE)
            add(LOCATION_FINE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                add(ACTIVITY_RECOGNITION)
            }
        }.toTypedArray()

        val LOCATION = buildList {
            add(LOCATION_COARSE)
            add(LOCATION_FINE)
        }.toTypedArray()

        val ACTIVITY = buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                add(ACTIVITY_RECOGNITION)
            }
        }.toTypedArray()
    }

    companion object {
        const val TAG = "RUNNING_ACTION_SERVICE"
        const val SERVICE_NAME = TAG
        const val SERVICE_NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = TAG

        const val RUN_ACTION_KEY = "runactionkey"
        const val UNDEFINED_RUN_ACTION_ID = Long.MIN_VALUE

        const val LOCATION_THREAD_NAME = "locationthreadname"
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        )
            .build()

        /**
         * Helper function to start the service
         * @param context context
         * @param runActionId id of the run action to start
         */
        fun start(
            context: Context,
            runActionId: Long
        ) {
            Intent(
                context,
                RunningActionService::class.java
            ).also { intent ->
                intent.action = RunningActionServiceActions.START.name

                intent.putExtra(RUN_ACTION_KEY, runActionId)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            }
        }

        /**
         * Helper function to stop the service
         * @param context context
         * @param runActionId id of the run action to stop, null to stop all
         */
        fun stop(
            context: Context,
            runActionId: Long?
        ) {
            Intent(
                context,
                RunningActionService::class.java
            ).also { intent ->
                intent.action = RunningActionServiceActions.STOP.name

                runActionId?.let {
                    intent.putExtra(RUN_ACTION_KEY, it)
                }

                context.startService(intent)
            }
        }
    }
}