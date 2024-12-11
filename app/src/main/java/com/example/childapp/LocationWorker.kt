package com.example.childapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val formattedLocation = "Широта: $latitude, Долгота: $longitude"

                    // Сохранение последнего местоположения
                    val sharedPref = applicationContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("last_location", formattedLocation).apply()

                    // Здесь можно добавить отправку координат на сервер (заглушка)
                }
            }
        } else {
            // Обработка отсутствия разрешений
            return Result.failure()
        }
        return Result.success()
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }
}
