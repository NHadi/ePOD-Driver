package sg.entvision.tmssystem.view.direction

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import sg.entvision.tmssystem.model.responses.MapResponse
import sg.entvision.tmssystem.repository.LocationRepository
import timber.log.Timber


class DirectionViewModel(var repository: LocationRepository) : ViewModel() {


    private val loc = MutableLiveData<MapResponse>()
    val getLocationUpdate = loc

    @SuppressLint("CheckResult")
    fun getRoute(startDestination: LatLng, destination: LatLng) {
        repository.getRoute(startDestination, destination)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.isSuccessful) {
                    Timber.e("Your Route : ${it.body()?.toString()}")
                    loc.value = it.body()
                }
            }, {
                Timber.e(it.printStackTrace().toString())
            })
    }
}