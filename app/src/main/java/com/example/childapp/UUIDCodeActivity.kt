package com.example.childapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.childapp.databinding.ActivityQrcodeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class UUIDCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrcodeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Уникальный идентификатор устройства
        val deviceUUID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val firstPart = deviceUUID.substring(0, 8)
        val lastPart = deviceUUID.substring(deviceUUID.length - 8)

        binding.textViewFirstPart.text = "Первая часть UUID: $firstPart"
        binding.textViewLastPart.text = "Последняя часть UUID: $lastPart"

        // Настройка для получения координат
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        displayLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun displayLastKnownLocation() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<android.location.Location> ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val formattedLocation = "Широта: $latitude, Долгота: $longitude"

                    // Сохранение последнего местоположения
                    val sharedPref = getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("last_location", formattedLocation).apply()

                    // Обновление UI
                    binding.textViewCoordinates.text = "Последние координаты: $formattedLocation"
                } else {
                    binding.textViewCoordinates.text = "Координаты отсутствуют"
                }
            }
        } else {
            binding.textViewCoordinates.text = "Нет разрешений на доступ к координатам"
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }
}
