package sg.entvision.tmssystem.utility

import org.json.JSONObject
import sg.entvision.tmssystem.consts.Consts
import timber.log.Timber

abstract class HandleNotification constructor(var data: JSONObject) {


    val JOB = "job"

    init {
        Timber.e("${data.optString(Consts.LAT)}")
        if (!data.optString(Consts.LAT).isNullOrEmpty()) {
            openTracking(
                data.optString(Consts.LAT).toDouble(),
                data.optString(Consts.LON).toDouble()
            )
        } else {
            Timber.e("open main")
            openMain()
        }
    }

    abstract fun openMain()
    abstract fun openTracking(lat: Double, lng: Double)

}