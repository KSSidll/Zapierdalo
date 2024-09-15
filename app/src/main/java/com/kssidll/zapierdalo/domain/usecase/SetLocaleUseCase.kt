package com.kssidll.zapierdalo.domain.usecase

import androidx.appcompat.app.AppCompatDelegate.setApplicationLocales
import androidx.core.os.LocaleListCompat
import com.kssidll.zapierdalo.domain.AppLocale

class SetLocaleUseCase {
    operator fun invoke(
        locale: AppLocale?
    ) {
        val localeList = if (locale != null) {
            LocaleListCompat.forLanguageTags(locale.tag)
        } else LocaleListCompat.getEmptyLocaleList()

        setApplicationLocales(localeList)
    }
}