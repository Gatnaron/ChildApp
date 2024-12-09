package com.example.childapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yandex.mapkit.geometry.Point
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class LocationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Здесь вы можете получить текущие координаты устройства
        val currentLocation = getCurrentLocation()

        // Отправка данных на сервер (пока что просто отображаем уведомление)
        showNotification(currentLocation)

        return Result.success()
    }

    private fun getCurrentLocation(): Point {
        // Реализуйте получение текущих координат устройства
        // Например, используя FusedLocationProvider или другой способ
        return Point(55.7558, 37.6173) // Пример координат
    }

    private fun showNotification(location: Point) {
        val notificationId = 1
        val channelId = "location_updates"

        // Создание канала уведомлений
        createNotificationChannel(channelId)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Обновление координат")
            .setContentText("Текущие координаты: ${location.latitude}, ${location.longitude}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Проверка разрешений перед отправкой уведомления
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Запрос разрешений, если они не предоставлены
            // Запрос разрешений должен быть выполнен в MainActivity или другой Activity
        } else {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, notificationBuilder.build())
            }
        }
    }

    private fun createNotificationChannel(channelId: String) {
        val name = "Location Updates"
        val descriptionText = "Channel for location updates"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
