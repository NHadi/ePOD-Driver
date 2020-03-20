package sg.entvision.tmssystem.view.direction

import com.google.android.gms.maps.model.LatLng
import kotlin.math.sign

class LinearFixed : DirectionRouteActivity.LatLngInterpolatorNew {
    override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
        val lat = (b.latitude - a.latitude) * fraction + a.latitude
        var lngDelta = b.longitude - a.longitude
        if (Math.abs(lngDelta) > 180) {
            lngDelta -= sign(lngDelta) * 360
        }
        val lng = lngDelta * fraction + a.longitude
        return LatLng(lat, lng)
    }
}