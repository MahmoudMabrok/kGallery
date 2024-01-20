package tools.mo3ta.kgallery.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import tools.mo3ta.kgallery.R


class ResizeWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    companion object {
        const val channelId = "resize_channel"
        const val NOTIFICATION_ID = 1
    }

    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Resize images")


    override suspend fun doWork(): Result {
        createNotificationChannel()

        Log.d("TestTest", "doWork: ")
        val foregroundInfo = ForegroundInfo(NOTIFICATION_ID, notificationBuilder.build())
        setForeground(foregroundInfo)

        (1..10).forEach {
            showProgress( it * 10)
            delay(1000)
        }

        return Result.success()
    }

    private suspend fun showProgress(progress: Int) {
        val notification = notificationBuilder
            .setProgress(100, progress, false)
            .build()
        val foregroundInfo = ForegroundInfo(NOTIFICATION_ID, notification)
        setForeground(foregroundInfo)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = notificationManager?.getNotificationChannel(channelId)
            if (notificationChannel == null) {
                notificationManager?.createNotificationChannel(
                    NotificationChannel(
                        channelId, "Resize", NotificationManager.IMPORTANCE_MIN
                    )
                )
            }
        }
    }

}
