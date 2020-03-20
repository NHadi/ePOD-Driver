package sg.entvision.tmssystem.view.direction

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.maps.android.PolyUtil
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.map_direction_activity.*
import sg.entvision.tmssystem.base.BaseMVVMActivity
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.atan
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.consts.Consts

class DirectionRouteActivity : BaseMVVMActivity<DirectionViewModel>(
    R.layout.map_direction_activity,
    DirectionViewModel::class
), OnMapReadyCallback,
    LocationListener,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {


    private lateinit var DELIVERY_LATLNG: LatLng
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) { // Update UI with location data
                mLastLocation = location
            }
        }
    }
    private var mLastLocation: Location? = null
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var fusedLocation: FusedLocationProviderClient
    private var mCurrLocation: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var latLng: LatLng? = null
    private var mMap: GoogleMap? = null


    private fun fetchLastLocation() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(this@DirectionRouteActivity)
        val task: Task<Location> = fusedLocation.lastLocation
        task.addOnSuccessListener { location ->
            mLastLocation = location
            latLng = LatLng(location.latitude, location.longitude)
            latLng?.let {
                viewModel.getRoute(it, DELIVERY_LATLNG)
            }
            if (location != null) {
                val mapFragment =
                    supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(this)
            } else {
                Toast.makeText(
                    this@DirectionRouteActivity,
                    "No Location recorded",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show()
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }


    fun loadNavigationView(lat: String, lng: String) {
        val navigation: Uri = Uri.parse("google.navigation:q=$lat,$lng")
        val navigationIntent = Intent(Intent.ACTION_VIEW, navigation)
        navigationIntent.setPackage("com.google.android.apps.maps")
        startActivity(navigationIntent)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
            val deliveryMarker =
                MarkerOptions().position(DELIVERY_LATLNG).title("Delivery Location")
            mMap?.let {
                it.apply {
                    isMyLocationEnabled = true
                    uiSettings?.isMapToolbarEnabled = false
                    uiSettings?.isCompassEnabled = false
//                    addMarker(deliveryMarker)
                    setOnMapClickListener {

                    }
                }
            }
            val markerOptions = MarkerOptions().icon(
                BitmapDescriptorFactory.fromBitmap(getIconMarkerAddress().makeIcon("Receipent Location"))
            ).position(DELIVERY_LATLNG)
                .anchor(getIconMarkerAddress().anchorU, getIconMarkerAddress().anchorV)
            mMap?.addMarker(markerOptions)
            mMap?.apply {
                animateCamera(CameraUpdateFactory.newLatLng(latLng))
                animateCamera(
                    CameraUpdateFactory.newLatLngZoom(latLng, 15F)
                )
                animateCamera(CameraUpdateFactory.newCameraPosition(latLng?.getCameraPosition()))
            }
        }
    }

    private fun LatLng.getCameraPosition(): CameraPosition {
        return CameraPosition.Builder().target(this).tilt(45f).zoom(15f).bearing(
            getBearing(
                this, DELIVERY_LATLNG
            )
        ).build()
    }

    override fun onConnected(p0: Bundle?) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show()
        mLastLocation?.let {
            mMap?.clear()
            latLng = LatLng(mLastLocation?.latitude!!, mLastLocation?.longitude!!)
            val markerOptions = MarkerOptions().also {
                it.position(latLng!!)
                it.title("Your Current Position")
                it.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            }
            mCurrLocation = mMap?.addMarker(markerOptions)


            val markerLabel = MarkerOptions().icon(
                BitmapDescriptorFactory.fromBitmap(getIconMarkerAddress().makeIcon("Your Location"))
            ).position(latLng!!)
                .anchor(getIconMarkerAddress().anchorU, getIconMarkerAddress().anchorV)
            mMap?.addMarker(markerLabel)

            mMap?.apply {
                animateCamera(CameraUpdateFactory.newLatLng(latLng))
                animateCamera(
                    CameraUpdateFactory.newLatLngZoom(latLng, 15F)
                )
                animateCamera(CameraUpdateFactory.newCameraPosition(latLng?.getCameraPosition()))
            }
        }
        mLocationRequest = LocationRequest().also {
            it.interval = 5000
            it.fastestInterval = 3000
            it.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        fusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    override fun viewReady(savedInstanceState: Bundle?) {
        title = "Delivery Route"
        val LAT = intent.getDoubleExtra(Consts.LAT,0.0)
        val LON = intent.getDoubleExtra(Consts.LON,0.0)
//        if (LAT != null && LON != null) {
            DELIVERY_LATLNG = LatLng(LAT, LON)
//        } else {
//            DELIVERY_LATLNG = LatLng(-6.202394, 106.652710)
//        }
        btnNavigate.setOnClickListener {
            loadNavigationView(
                DELIVERY_LATLNG.latitude.toString(),
                DELIVERY_LATLNG.longitude.toString()
            )
        }
        fetchLastLocation()
        buildGoogleApiClient()
        viewModel.getLocationUpdate.observe(this@DirectionRouteActivity, Observer {
            var lineOptions = PolylineOptions()
            it.routes.forEach {
                val decodedPath: List<LatLng> = PolyUtil.decode(it.overview_polyline.points)
                lineOptions = PolylineOptions().also { its ->
                    its.addAll(decodedPath)
                    its.width(20f)
                    its.color(Color.parseColor("#008577"))
                    its.geodesic(true)
                }
            }
            mMap?.addPolyline(lineOptions)
            Timber.e("response received ${it.geocoded_waypoints.size}")
            Toast.makeText(
                this,
                "Location Changed ${it.geocoded_waypoints.size}",
                Toast.LENGTH_SHORT
            ).show()
        })
    }


    private fun getIconMarkerAddress(): IconGenerator {
        val icg = IconGenerator(this)
        icg.setColor(Color.WHITE)
        icg.setTextAppearance(R.style.BlackText)
        return icg
    }

    override fun onLocationChanged(location: Location?) {
//        if (mCurrLocation != null) {
//            mCurrLocation!!.remove()
//        }
        latLng = LatLng(location?.latitude!!, location.longitude)
        val driverMarker = MarkerOptions().also { marker ->
            marker.position(latLng!!)
            marker.title("Current Position")
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        }
        mCurrLocation = mMap?.addMarker(driverMarker)
        animateMarkerNew(DELIVERY_LATLNG, mCurrLocation)
//        mMap?.apply {
//            animateCamera(CameraUpdateFactory.newLatLng(latLng))
//            animateCamera(
//                CameraUpdateFactory.newLatLngZoom(latLng, 15F)
//            )
//        }
        latLng?.let {
            viewModel.getRoute(it, DELIVERY_LATLNG)
        }
    }

    override fun onPause() {
        super.onPause()
        //Unregister for location callbacks:
        if (mGoogleApiClient != null) {
            fusedLocation.removeLocationUpdates(mLocationCallback)
        }
    }


    private fun animateMarkerNew(
        destination: LatLng,
        marker: Marker?
    ) {
        if (marker != null) {
            val startPosition = marker.position
            val latLngInterpolator: LatLngInterpolatorNew = LinearFixed()
            val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            valueAnimator.duration = 3000 // duration 3 second
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener { animation ->
                try {
                    val v = animation.animatedFraction
                    val newPosition: LatLng =
                        latLngInterpolator.interpolate(v, startPosition, destination)
                    marker.position = newPosition
                    mMap?.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(newPosition)
                                .tilt(45f)
                                .zoom(15f)
                                .build()
                        )
                    )
                    marker.rotation = getBearing(
                        startPosition, destination
                    )
                } catch (ex: Exception) {
                    //I don't care atm..
                }
            }
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    // if (mMarker != null) {
                    // mMarker.remove();
                    // }
                    // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
                }
            })
            valueAnimator.start()
        }
    }


    private fun getBearing(begin: LatLng, end: LatLng): Float {
        val lat = abs(begin.latitude - end.latitude)
        val lng = abs(begin.longitude - end.longitude)
        if (begin.latitude < end.latitude && begin.longitude < end.longitude) return Math.toDegrees(
            atan(lng / lat)
        )
            .toFloat() else if (begin.latitude >= end.latitude && begin.longitude < end.longitude) return (90 - Math.toDegrees(
            atan(lng / lat)
        ) + 90).toFloat() else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude) return (Math.toDegrees(
            atan(lng / lat)
        ) + 180).toFloat() else if (begin.latitude < end.latitude && begin.longitude >= end.longitude) return (90 - Math.toDegrees(
            atan(lng / lat)
        ) + 270).toFloat()
        return (-1).toFloat()
    }

    interface LatLngInterpolatorNew {
        fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng
    }
}