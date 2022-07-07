package org.evolveapp.marathoner.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

object RuntimeLocaleChanger {

    fun wrapContext(context: Context): Context {

        val lan = context.prefDao.language

        val savedLocale = Locale(lan)

        // as part of creating a new context that contains the new locale we also need to override the default locale.
        Locale.setDefault(savedLocale)

        // create new configuration with the saved locale
        val newConfig = Configuration()
        newConfig.setLocale(savedLocale)

        return context.createConfigurationContext(newConfig)

    }

}