package com.kssidll.zapierdalo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kssidll.zapierdalo.service.RunningActionService
import com.kssidll.zapierdalo.service.getServiceStateCold
import com.kssidll.zapierdalo.service.getSharedPreferences
import com.kssidll.zapierdalo.service.setServiceState
import com.kssidll.zapierdalo.ui.theme.ZapierdaloTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        runBlocking {
            applicationContext.setServiceState(
                RunningActionService.SERVICE_NAME,
                applicationContext.getServiceStateCold(RunningActionService::class.java)
            )
        }

        Configuration.getInstance()
            .load(applicationContext, applicationContext.getSharedPreferences("OSMMAP_PREFERENCES"))
        Configuration.getInstance().userAgentValue = applicationContext.packageName

        enableEdgeToEdge()

        setContent {
            ZapierdaloTheme {
                Navigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

