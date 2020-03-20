package sg.entvision.tmssystem.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.model.responses.MapResponse
import sg.entvision.tmssystem.network.ApiService
import sg.entvision.tmssystem.network.MapService
import sg.entvision.tmssystem.utility.extentions.checkLocationPermission
import sg.entvision.tmssystem.utility.extentions.isGPSEnabled
import timber.log.Timber

class LocationRepository constructor(
    private val application: Application,
    private val database: Database,
    private val api: MapService
) : KoinComponent {


    @SuppressLint("CheckResult")
    fun getRoute(origin: LatLng, destination: LatLng): Single<Response<MapResponse>> {
        return api.getRoute(
            "${origin.latitude},${origin.longitude}",
            "${destination.latitude},${destination.longitude}",
            "driving", "true", application.getString(R.string.API_KEY)
        )
    }


    @SuppressLint("MissingPermission")
    fun getLocation() {
        /*
         * One time location request
         */
        if (application.isGPSEnabled() && application.checkLocationPermission()) {
            LocationServices.getFusedLocationProviderClient(application)
                ?.lastLocation
                ?.addOnSuccessListener { location: android.location.Location? ->
                    if (location != null)
//                        saveLocation(
//                            LocationTable(
//                                0,
//                                location.latitude,
//                                location.longitude,
//                                System.currentTimeMillis()
//                            )
//                        )
                        Timber.e("Location Tracked ${location.latitude} : ${location.longitude}")
                    hitApi()
                }
        }
    }

    @SuppressLint("CheckResult")
    private fun hitApi() {
        val apiService: ApiService by inject()
        apiService.hitAPI().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e(it.body().toString())
            }, {
                it.printStackTrace()
            })
    }

    private fun saveLocation(location: LocationTable) =
        GlobalScope.launch { database.locationDao().insert(location) }

    fun getSavedLocation(): Flowable<List<LocationTable>> = database.locationDao().selectAll()
}