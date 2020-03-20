package sg.entvision.tmssystem.view.delivery_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_item_detail.*
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.consts.Consts
import sg.entvision.tmssystem.view.ContainerActivity
import sg.entvision.tmssystem.view.direction.DirectionRouteActivity


/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ContainerActivity].
 */
class ItemDetailActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    private var latLng: LatLng? = null
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(detail_toolbar)
        if (intent.getIntExtra(Consts.TYPE, 0) == 90) {
            fab.visibility = View.GONE
        }

        fab.isIconAnimated = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            val fragment = ItemDetailFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(
                            ItemDetailFragment.ARG_ITEM_ID,
                            intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID)
                        )
                        putInt(Consts.TYPE, intent.getIntExtra(Consts.TYPE, 0))
                    }
                }

            supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit()

            fetchLastLocation()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, ContainerActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    private fun fetchLastLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@ItemDetailActivity)
        val task: Task<Location> =
            fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                latLng = LatLng(location.latitude, location.longitude)
                val mapFragment =
                    supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(this)
            } else {
                Toast.makeText(this@ItemDetailActivity, "No Location recorded", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
            val markerOptions =
                latLng?.let { MarkerOptions().position(it).title("Delivery Location") }
            mMap?.let {
                it.apply {
                    animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            latLng,
                            15F
                        )
                    )
                    uiSettings?.isMapToolbarEnabled = false
                    addMarker(markerOptions)
                    setOnMapClickListener {
                        routeToLocation()
                    }
                }
            }
        }
    }

    private fun routeToLocation() {
        startActivity(
            Intent(
                this@ItemDetailActivity,
                DirectionRouteActivity::class.java
            ).putExtra("LAT", latLng?.latitude).putExtra("LON", latLng?.longitude)
        )
    }

    override fun onConnected(p0: Bundle?) {
        var location = LocationServices.getFusedLocationProviderClient(this@ItemDetailActivity)
        location.lastLocation.addOnSuccessListener {
            mMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    15F
                )
            )
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


}
