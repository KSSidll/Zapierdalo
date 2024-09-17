package com.kssidll.zapierdalo

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kssidll.zapierdalo.domain.preference.AppPreferences
import com.kssidll.zapierdalo.domain.preference.detectDarkMode
import com.kssidll.zapierdalo.service.RunningActionService
import com.kssidll.zapierdalo.service.getServiceStateCold
import com.kssidll.zapierdalo.service.getSharedPreferences
import com.kssidll.zapierdalo.service.setServiceState
import com.kssidll.zapierdalo.ui.theme.ZapierdaloTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val colorScheme: AppPreferences.Theme.ColorScheme.Values
        val isInDynamicColor: Boolean

        runBlocking {
            applicationContext.setServiceState(
                RunningActionService.SERVICE_NAME,
                applicationContext.getServiceStateCold(RunningActionService::class.java)
            )

            colorScheme = AppPreferences.getColorScheme(applicationContext).first()
            isInDynamicColor = AppPreferences.getDynamicColor(applicationContext).first()
        }

        Configuration.getInstance()
            .load(applicationContext, applicationContext.getSharedPreferences("OSMMAP_PREFERENCES"))
        Configuration.getInstance().userAgentValue = applicationContext.packageName

        setContent {
            val appColorScheme =
                AppPreferences.getColorScheme(applicationContext).collectAsState(colorScheme).value

            ZapierdaloTheme(
                appColorScheme = appColorScheme,
                isInDynamicColor = AppPreferences.getDynamicColor(applicationContext)
                    .collectAsState(isInDynamicColor).value
            ) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                        appColorScheme.detectDarkMode()
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                        Color.argb(0x80, 0x1b, 0x1b, 0x1b),
                        appColorScheme.detectDarkMode()
                    )
                )

                Navigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

