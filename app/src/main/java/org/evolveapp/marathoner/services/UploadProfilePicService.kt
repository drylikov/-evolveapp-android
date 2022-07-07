package org.evolveapp.marathoner.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.evolveapp.domain.usecases.profile.UpdateProfilePhotoUseCase
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.ui.splash.SplashActivity
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class UploadProfilePicService : Service() {

    @Inject
    lateinit var updateProfilePhotoUseCase: UpdateProfilePhotoUseCase

    private var photoPath: String? = null

    companion object {

        var isRunning = false

        const val IS_SUCCESS = "is_success"
        const val PHOTO_URL = "photo_url"

        const val ACTION_RECEIVE_RESPONSE = "action_receive_response"

        private const val CHANNEL_ID = "CHANNEL_ID"

        const val EXTRA_IMAGE_PATH = "image_path"

    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true

    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false

    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand()")

        createNotificationChannel()

        //createNotificationChannel()
        val notificationIntent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )


        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {

            setSmallIcon(R.drawable.ic_round_file_upload_24)
            setContentTitle("Uploading Profile Picture")
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)
            setContentIntent(pendingIntent)

            //val defaultNotificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            //setSound(defaultNotificationSound)

        }

        startForeground(1, builder.build())

        photoPath = intent?.getStringExtra(EXTRA_IMAGE_PATH)

        CoroutineScope(Dispatchers.IO).launch {

            val compressedImage = compressImage(photoPath ?: "")

            val returnedIntent = Intent(ACTION_RECEIVE_RESPONSE)

            val response = updateProfilePhotoUseCase.launch(compressedImage).first()

            response.onSuccess { profile ->
                returnedIntent.putExtra(IS_SUCCESS, true)
                returnedIntent.putExtra(PHOTO_URL, profile.avatar ?: "")
            }

            response.onFailure { message, code ->
                returnedIntent.putExtra(IS_SUCCESS, false)
            }

            sendBroadcast(returnedIntent)
            stopSelf()

        }

        return START_NOT_STICKY

    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Upload Profile Picture",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)

            manager?.createNotificationChannel(serviceChannel)

        }

    }

    private suspend fun compressImage(imagePath: String): File {

        val originalImage = File(imagePath)

        return Compressor.compress(this, originalImage)

    }

}