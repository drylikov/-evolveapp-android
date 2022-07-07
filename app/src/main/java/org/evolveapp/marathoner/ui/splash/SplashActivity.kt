package org.evolveapp.marathoner.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.ui.main.MainActivity
import org.evolveapp.marathoner.utils.RuntimeLocaleChanger
import org.evolveapp.marathoner.utils.goTo
import org.evolveapp.marathoner.utils.prefDao
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // FIXME: 10/7/2021 Remove this line
        Timber.d("accessToken: " + prefDao.accessToken)
        Timber.d("refreshToken: " + prefDao.refreshToken)

        //prefDao.accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjM0NTM5NTE3LCJqdGkiOiJmMjVhN2FmNjAwZjU0MTUyYTZkZDY0YzI0NDkwY2U5NiIsInVzZXJfaWQiOjE3Nn0.Wjv_EEU8G0GPULtL7rIMHknXqLKWXXX8eqa06NZQvGw"

        Handler(Looper.getMainLooper()).postDelayed({ init() }, 500)

    }

    private fun init() {

        goTo(MainActivity::class.java)
        finishAffinity()

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(base))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim)
    }

}