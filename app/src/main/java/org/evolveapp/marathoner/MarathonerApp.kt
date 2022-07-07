package org.evolveapp.marathoner

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import org.evolveapp.marathoner.utils.RuntimeLocaleChanger
import timber.log.Timber

@HiltAndroidApp
class MarathonerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(base))
    }

}