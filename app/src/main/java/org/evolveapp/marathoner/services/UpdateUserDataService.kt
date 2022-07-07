package org.evolveapp.marathoner.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.evolveapp.data.local.PrefDao
import org.evolveapp.domain.repositories.ProfileRepository
import org.evolveapp.marathoner.R
import timber.log.Timber
import javax.inject.Inject


private const val TAG = "tag_ser_data"

@AndroidEntryPoint
class UpdateUserDataService : Service() {

    @Inject
    lateinit var profileRepository: ProfileRepository

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {

            setSmallIcon(R.drawable.ic_download)
            setContentTitle(getString(R.string.updating_user_data))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)

        }

        startForeground(1, builder.build())

        CoroutineScope(Dispatchers.IO).launch {
            fetchAndSaveUserData()
        }

        return START_STICKY
    }

    private suspend fun fetchAndSaveUserData() {

        val response = profileRepository.fetchProfileInfo().first()

        response.onSuccess { profile ->

            PrefDao.getInstance(this).apply {

                firstName = profile.firstName
                lastName = profile.lastName
                username = profile.username
                userId = profile.id.toString()
                userEmail = profile.email
                userPhoto = profile.avatar
                dateOfBirth = profile.dateOfBirthday
                country = profile.country

            }

            stopSelf()
        }

        response.onFailure { message, _ ->

            stopSelf()

            Timber.tag(TAG).e("api-error: $message")


        }

    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.updating_user_data),
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)

            manager?.createNotificationChannel(serviceChannel)

        }

    }

    companion object {

        private const val CHANNEL_ID = "CHANNEL_ID"

        fun launch(context: Context) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, UpdateUserDataService::class.java))
            } else {
                context.startService(Intent(context, UpdateUserDataService::class.java))
            }

        }

    }

}