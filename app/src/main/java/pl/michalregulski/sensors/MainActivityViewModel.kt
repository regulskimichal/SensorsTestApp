package pl.michalregulski.sensors

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivityViewModel(
    application: Application,
    private val locationService: LocationService
) : AndroidViewModel(application) {

    val latitude = Observable("")
    val longitude = Observable("")
    val sensorNames = Observable(listOf<String>())

    @RequiresPermission(anyOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    fun updateLocation() {
        viewModelScope.launch {
            val currentLocation = locationService.getCurrentLocation()
            latitude.set(currentLocation?.latitude?.toString() ?: "")
            longitude.set(currentLocation?.longitude?.toString() ?: "")
        }
    }

    fun updateSensors() {
        val sensorManager = getSystemService<SensorManager>(getApplication())

        sensorNames.set(sensorManager?.getSensorList(Sensor.TYPE_ALL)
            ?.map { it.name }
            ?: listOf()
        )
    }

}
