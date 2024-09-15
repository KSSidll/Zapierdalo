package com.kssidll.zapierdalo.ui.screen.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kssidll.zapierdalo.ExpandedPreviews
import com.kssidll.zapierdalo.R
import com.kssidll.zapierdalo.helper.none
import com.kssidll.zapierdalo.ui.component.LanguageExposedDropdown
import com.kssidll.zapierdalo.ui.component.SecondaryAppBar
import com.kssidll.zapierdalo.ui.theme.Typography
import com.kssidll.zapierdalo.ui.theme.ZapierdaloTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onEvent: (event: SettingsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            SecondaryAppBar(
                onBack = {
                    onEvent(SettingsEvent.NavigateBack)
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = Typography.titleLarge,
                    )
                },
            )
        },
        contentWindowInsets = WindowInsets.none,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                LanguageExposedDropdown(
                    setLocale = {
                        onEvent(SettingsEvent.SetLocale(it))
                    }
                )
            }
        }
    }
}

@PreviewLightDark
@ExpandedPreviews
@Composable
private fun SettingsScreenPreview() {
    ZapierdaloTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SettingsScreen(
                onEvent = {}
            )
        }
    }
}
