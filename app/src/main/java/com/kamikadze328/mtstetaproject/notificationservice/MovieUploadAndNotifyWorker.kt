package com.kamikadze328.mtstetaproject.notificationservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.request.ImageRequest
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.presentation.main.MainActivity
import com.kamikadze328.mtstetaproject.presentation.moviedetails.MovieDetailsFragmentArgs
import com.kamikadze328.mtstetaproject.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MovieUploadAndNotifyWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    val movieRepository: MovieRepository
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val CHANNEL_ID = "default_01"
    }

    override suspend fun doWork(): Result {
        try {
            val movie: Movie? = movieRepository.loadRandomPopularMovie()
            movie?.let {
                val req = ImageRequest.Builder(appContext)
                    .data(it.poster_path)
                    .target { result ->
                        val bitmap = (result as BitmapDrawable).bitmap
                        sendNotificationToMovie(it.title, it.id, bitmap)
                    }
                    .build()
                ImageLoader(appContext).execute(req)

            }
        } catch (e: Exception) {
            val message = workerParams.inputData.getString(MyFirebaseMessagingService.MESSAGE_ARG)
                ?: appContext.resources.getString(R.string.fcm_message_body)
            sendNotification(message)
            return Result.failure()
        }
        return Result.success()

    }

    private fun sendNotificationToMovie(movieName: String, movieId: Int, movieIcon: Bitmap) {
        val args = MovieDetailsFragmentArgs.Builder(movieId).build().toBundle()

        val pendingIntent = NavDeepLinkBuilder(appContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.navigation)
            .setDestination(R.id.navigation_movie_details)
            .setArguments(args)
            .createPendingIntent()

        sendNotification(movieName, pendingIntent, movieIcon)
    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        sendNotification(messageBody, pendingIntent)
    }

    private fun sendNotification(
        messageBody: String,
        intent: PendingIntent,
        largeImage: Bitmap? = null
    ) {
        val channelName = appContext.resources.getString(R.string.default_notification_channel_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationId = 0

        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(appContext.resources.getString(R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setLargeIcon(largeImage)
            .setContentIntent(intent)

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}