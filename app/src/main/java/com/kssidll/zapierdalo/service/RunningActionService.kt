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
import android.os.IBinder
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
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
import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.domain.usecase.gps.InsertGpsEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.InsertRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.UpdateRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.GetStepsEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.InsertStepsEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.UpdateStepsEntityUseCase
import com.kssidll.zapierdalo.helper.checkPermission
import com.kssidll.zapierdalo.helper.getLocalizedString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * Possible actions that the [RunningActionService] can perform
 */
enum class RunningActionServiceActions {
    START,
    PAUSE,
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
            val locationPermissions = permissionResultMap.all {
                it.key in RunningActionService.Permissions.LOCATION && it.value
            }

            val activitiyPermissions = permissionResultMap.all {
                it.key in RunningActionService.Permissions.ACTIVITY && it.value
            }

            if (locationPermissions) {
                requestLocationSetting()
            } else if (activitiyPermissions) {
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

    private var sensorManager: SensorManager? = null

    @Inject
    lateinit var insertGpsEntityUseCase: InsertGpsEntityUseCase

    @Inject
    lateinit var insertRunActionEntityUseCase: InsertRunActionEntityUseCase

    @Inject
    lateinit var getRunActionEntityUseCase: GetRunActionEntityUseCase

    @Inject
    lateinit var updateRunActionEntityUseCase: UpdateRunActionEntityUseCase

    @Inject
    lateinit var insertStepsEntityUseCase: InsertStepsEntityUseCase

    @Inject
    lateinit var getStepsEntityUseCase: GetStepsEntityUseCase

    @Inject
    lateinit var updateStepsEntityUseCase: UpdateStepsEntityUseCase

    private var runActionId: Long? = null
    private var stepsId: Long? = null
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
        Log.d(
            TAG,
            "onStartCommand: Executed with startId: $startId"
        )

        if (intent != null) {
            when (intent.action) {
                RunningActionServiceActions.START.name -> startAction()
                RunningActionServiceActions.PAUSE.name -> TODO()
                RunningActionServiceActions.STOP.name -> stopAction()
                else -> Log.e(
                    TAG,
                    "onStartCommand: No action in the received intent"
                )
            }
        } else {
            Log.e(
                TAG,
                "onStartCommand: No intent"
            )
        }

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(
            TAG,
            "onCreate: Service created"
        )

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
        super.onDestroy()

        Log.d(
            TAG,
            "onDestroy: Service destroyed"
        )

        unregisterReceiver(localeChangeReceiver)
        sensorManager?.unregisterListener(this)
        serviceJob.cancel()
    }

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

    private fun startAction() {
        Log.d(
            TAG,
            "startAction: Attempting to start"
        )

        // early return if already started
        if (getServiceState(SERVICE_NAME) == ServiceState.STARTED) {
            Log.d(
                TAG,
                "startAction: Service already set as running"
            )
            return
        }

        // set as started
        setServiceState(
            SERVICE_NAME,
            ServiceState.STARTED
        )

        Log.d(
            TAG,
            "startAction: Started"
        )

        init()

        if (checkPermission(applicationContext, Permissions.ACTIVITY.asList())) {
            stepCounterSensor?.let { stepCounterSensor ->
                sensorManager?.registerListener(
                    this,
                    stepCounterSensor,
                    SensorManager.SENSOR_DELAY_FASTEST
                )
            }
        }

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    Log.d(
                        TAG,
                        "locationCallback: received location"
                    )

                    serviceScope.launch {
                        if (runActionId == null) {
                            runActionId = insertRunActionEntityUseCase(
                                RunActionEntity()
                            )
                        }

                        val entity = GpsEntity(
                            runActionId = runActionId!!,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            altitude = location.altitude,
                            accuracy = location.accuracy,
                            speed = location.speed,
                        )

                        insertGpsEntityUseCase(entity)
                    }
                }
            }
        }

        if (checkPermission(applicationContext, Permissions.LOCATION.asList())) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                mainLooper
            )
        }
    }

    private fun stopAction() {
        Log.d(
            TAG,
            "stopAction: Stopping the service"
        )

        stop()
    }

    private fun stop() {
        Log.d(
            TAG,
            "stop: Stopping the service"
        )

        setServiceState(
            SERVICE_NAME,
            ServiceState.STOPPED
        )

        try {
            serviceScope.launch {
                stepsId?.let { stepsId ->
                    val stepsEntity = getStepsEntityUseCase(stepsId).first()!!.copy(
                        endTimestamp = Calendar.getInstance().timeInMillis
                    )

                    updateStepsEntityUseCase(stepsEntity)
                }

                runActionId?.let { runActionId ->
                    val runActionEntity =
                        getRunActionEntityUseCase(runActionId).first()!!.copy(
                            endTimestamp = Calendar.getInstance().timeInMillis
                        )

                    updateRunActionEntityUseCase(runActionEntity)
                }
            }.invokeOnCompletion {
                fusedLocationClient.removeLocationUpdates(locationCallback!!)

                ServiceCompat.stopForeground(
                    this,
                    ServiceCompat.STOP_FOREGROUND_REMOVE
                )

                stopSelf()
            }
        } catch (e: Exception) {
            Log.d(
                TAG,
                "stop: Service stopped without being started: ${e.message}"
            )
        }
    }

    /**
     * Creates the notification channel for the service
     *
     * Has to be called before any notifications can be posted
     */
    private fun createNotificationChannel() {
        Log.d(
            TAG,
            "createNotificationChannel: creating"
        )

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        notificationManager.createNotificationChannel(makeNotificationChannel())
    }

    /**
     * Updates the notification channel for the service
     *
     * Changes the name and the description to current locale
     */
    private fun updateNotificationChannel() {
        Log.d(
            TAG,
            "updateNotificationChannel: updating"
        )

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        notificationManager.createNotificationChannel(makeNotificationChannel())
    }

    /**
     * @return notification channel for the service
     */
    private fun makeNotificationChannel(): NotificationChannelCompat {
        Log.d(
            TAG,
            "makeNotificationChannel: making"
        )

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
        Log.d(
            TAG,
            "createNotification: creating"
        )

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
                    Log.d(
                        TAG,
                        "stepsCallback: received data"
                    )

                    val steps = event.values.lastOrNull()?.toLong() ?: return

                    serviceScope.launch {
                        if (runActionId == null) {
                            runActionId = insertRunActionEntityUseCase(
                                RunActionEntity()
                            )
                        }

                        if (stepsId == null) {
                            stepsId = insertStepsEntityUseCase(
                                StepsEntity(runActionId = runActionId!!)
                            )

                            stepsStartCount = steps
                        } else {
                            val entity = getStepsEntityUseCase(stepsId!!).first()!!.copy(
                                steps = steps - stepsStartCount!!
                            )

                            updateStepsEntityUseCase(entity)
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


        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        )
            .build()

        /**
         * Helper function to start the service
         * @param context context
         */
        fun start(
            context: Context,
        ) {
            Intent(
                context,
                RunningActionService::class.java
            ).also { intent ->
                intent.action = RunningActionServiceActions.START.name

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
         */
        fun stop(
            context: Context,
        ) {
            Intent(
                context,
                RunningActionService::class.java
            ).also {
                it.action = RunningActionServiceActions.STOP.name

                context.startService(it)
            }
        }
    }
}