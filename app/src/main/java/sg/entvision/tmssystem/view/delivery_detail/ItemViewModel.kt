package sg.entvision.tmssystem.view.delivery_detail

import android.app.Application
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import sg.entvision.tmssystem.consts.Consts
import sg.entvision.tmssystem.model.Response
import sg.entvision.tmssystem.repository.LocationRepository
import sg.entvision.tmssystem.utility.workmanager.TrackLocationWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ItemViewModel(var application: Application,
                    var repository: LocationRepository) : ViewModel() {


    fun trackLocation() {
        val locationWorker = PeriodicWorkRequestBuilder<TrackLocationWorker>(3, TimeUnit.MINUTES).addTag(
            Consts.LOCATION_WORK_TAG
        ).build()
        WorkManager
            .getInstance()
            .enqueueUniquePeriodicWork(Consts.LOCATION_WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, locationWorker)
    }

    fun stopTrackLocation() {
        WorkManager.getInstance().cancelAllWorkByTag(Consts.LOCATION_WORK_TAG)
    }

    val enableLocation: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val location: MutableLiveData<Response<List<Location>>> = MutableLiveData()

    fun locationSetup() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        enableLocation.value = Response.loading()
        LocationServices.getSettingsClient(application)
            .checkLocationSettings(
                LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .setAlwaysShow(true)
                    .build())
            .addOnSuccessListener { enableLocation.value = Response.success(true) }
            .addOnFailureListener {
                Timber.e(it, "Gps not enabled")
                enableLocation.value = Response.error(it)
            }
    }
}