package com.kssidll.zapierdalo.ui.screen.settings


import androidx.lifecycle.ViewModel
import com.kssidll.zapierdalo.domain.AppLocale
import com.kssidll.zapierdalo.domain.usecase.SetLocaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class SettingsEvent {
    data class SetLocale(val locale: AppLocale?): SettingsEvent()
    data object NavigateBack: SettingsEvent()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setLocaleUseCase: SetLocaleUseCase
): ViewModel() {

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetLocale -> setLocale(event.locale)

            is SettingsEvent.NavigateBack -> {}
        }
    }

    private fun setLocale(locale: AppLocale?) {
        setLocaleUseCase(locale)
    }
}
