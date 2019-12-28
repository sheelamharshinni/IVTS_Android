package com.tecdatum.Tracking.School.newactivities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tecdatum.Tracking.School.Constants.remote.IGoogleApi
import com.tecdatum.Tracking.School.MapsActivity
import com.tecdatum.Tracking.School.R
import com.tecdatum.Tracking.School.fragments.ExceptionReportFragment
import kotlinx.android.synthetic.main.activity_holiday.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class HolidayActivity : AppCompatActivity(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var currentLocationMarker: Marker? = null
    private var currentLocation: Location? = null
    private var firstTimeFlag = true
    var VehicleName: String? = null
    var statuscolor: String? = null
    var sincefrom: String? = null
    var ignition: String? = null
    var speed: String? = null
    var liveedate: String? = null
    var vehicletype: String? = null
    var DriverName: String? = null
    var loc: String? = null
    var MobNo: String? = null
    var timer = Timer()
    var StartTime: String? = null
    var EndTime: String? = null
    var VechileId: String? = null
    private var apiInterface: IGoogleApi? = null
    var line: Polyline? = null
    var lat = 17.3850
    var lng = 78.4867
    var direction: String? = null

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult.getLastLocation() == null) return
            currentLocation = locationResult.getLastLocation()
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation)
                firstTimeFlag = false
            }
            showMarker(currentLocation)
        }
    }

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)
        statusCheck()
        service()

        val bb = getSharedPreferences(SplashScreen.MY_PREFS_NAME, Context.MODE_PRIVATE)
        StartTime = bb.getString("StartTime", "")
        EndTime = bb.getString("EndTime", "")
        val supportMapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.supportMap) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)
        navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.navigation_Profile -> {
                    val intent = Intent(this@HolidayActivity, Edit_ParentDetails::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_Logout -> {
                    logoutdialog()

                    true


                }
                R.id.navigation_history -> {
                    val intent = Intent(this@HolidayActivity, PArentHistoryTrackingDetails::class.java)
                    startActivity(intent)
                    true


                }
                else -> false
            }
        }
        lay_back.visibility = View.INVISIBLE
        lay_back.isEnabled = false
        try {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    val connec = this@HolidayActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if ((connec != null && (((connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() === NetworkInfo.State.CONNECTED) || (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() === NetworkInfo.State.CONNECTED))))) {

                        val c = Calendar.getInstance()
                        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)



                        if (timeOfDay > Integer.valueOf(StartTime!!) && timeOfDay < Integer.valueOf(EndTime!!)) { //checkes whether the current time is between 14:49:00 and 20:11:13.
                            println(true)
                            try {
                                finish()
                                timer.cancel()
                                val i = Intent(applicationContext, MapsActivity::class.java)
                                startActivity(i)

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        } else {

                            service()
                        }


                    } else if ((connec != null && (((connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() === NetworkInfo.State.DISCONNECTED) || (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() === NetworkInfo.State.DISCONNECTED))))) {

                    }
                }
            }, 1, 1500)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    fun service() {
        val bb: SharedPreferences = getSharedPreferences(SplashScreen.MY_PREFS_NAME, Context.MODE_PRIVATE)
        VechileId = bb.getString("VehicleID", "")

        try {
            val jsonBody = JSONObject()
            jsonBody.put("VehicleId", "" + VechileId)
            Log.e("RequestBody", "Request Body:- \${reqobject}")
            val mediaType = MediaType.parse("application/json")
            val requestBody = RequestBody.create(mediaType, jsonBody.toString())
            val retrofit2 = Retrofit.Builder()
                    .baseUrl("http://tecdatum.net/IVTSSchools/api/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
            apiInterface = retrofit2.create<IGoogleApi>(IGoogleApi::class.java)
            apiInterface!!.Places(requestBody)
                    ?.enqueue(object : Callback<String?> {
                        override fun onResponse(call: Call<String?>, response: Response<String?>) {
                            try {
                                val jsonObject = JSONObject(response.body())
                                Log.e("response", "" + jsonObject)
                                if (line != null) {
                                    line!!.remove()
                                }
                                val code = jsonObject.getString("Code")

                                if (code.equals("0", ignoreCase = true)) {
                                    val jsonArray = jsonObject.getJSONArray("LivevehicleDetails")
                                    for (i in 0 until jsonArray.length()) {
                                        val route = jsonArray.getJSONObject(i)
                                        val Lattitude = route.getString("Lattitude")
                                        val Longitude = route.getString("Longitude")
                                        lat = Lattitude.toDouble()
                                        lng = Longitude.toDouble()

                                        VehicleName = route.getString("VehicleName").toString()
                                        speed = route.getString("Speed").toString()
                                        loc = route.getString("Location").toString()
                                        statuscolor = route.getString("fromStatus").toString()
                                        direction = route.getString("Direction").toString()
                                        sincefrom = route.getString("SinceFrom").toString()
                                        ignition = route.getString("Ignition").toString()
                                        liveedate = route.getString("LiveDate").toString()
                                        vehicletype = route.getString("VehicleType").toString()
                                        DriverName = route.getString("DriverName").toString()
                                        MobNo = route.getString("MobileNo").toString()
                                        VechileId = route.getString("VehicleId").toString()


                                        Log.e("url Speed", "$lat,$lng")

                                    }


                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: Call<String?>, t: Throwable) {
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startCurrentLocationUpdates() {
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(3000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@HolidayActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
                return
            }
        }
        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper())
    }

    private val isGooglePlayServicesAvailable: Boolean
        private get() {
            val googleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
            val status: Int = googleApiAvailability.isGooglePlayServicesAvailable(this)
            if (ConnectionResult.SUCCESS === status) return true else {
                if (googleApiAvailability.isUserResolvableError(status)) Toast.makeText(this, "Please Install google play services to use this application", Toast.LENGTH_LONG).show()
            }
            return false
        }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) Toast.makeText(this, "Permission denied by uses", Toast.LENGTH_SHORT).show() else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) startCurrentLocationUpdates()
        }
    }

    private fun animateCamera(@NonNull location: Location?) {
        val latLng = LatLng(lat, lng)
        googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)))
    }

    @NonNull
    private fun getCameraPositionWithBearing(latLng: LatLng): CameraPosition {
        return CameraPosition.Builder().target(latLng).zoom(16F).build()
    }

    private fun showMarker(@NonNull currentLocation: Location?) {
        val Title: String = VehicleName + "_" + statuscolor + "_" + ignition + "_" + sincefrom + "_" +
                speed + "_" + liveedate + "_" + DriverName + "_" + MobNo + "_" + loc


        val latLng = LatLng(lat, lng)
        if (currentLocationMarker == null) currentLocationMarker = googleMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_fortracking)).position(latLng).title(Title))

        val customInfoWindow = MapsActivity.CustomInfoWindow(layoutInflater)
        googleMap!!.setInfoWindowAdapter(customInfoWindow)
        MapsActivity.markerPlaces1.add(currentLocationMarker!!.getId())

    }

    override fun onStop() {
        super.onStop()
        if (fusedLocationProviderClient != null) fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (isGooglePlayServicesAvailable) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            startCurrentLocationUpdates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient = null
        googleMap = null
    }

    fun statusCheck() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                })
                .setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        statusCheck()

                    }
                })
        val alert = builder.create()
        alert.show()
    }

    fun logoutdialog() {
        val dialogs = Dialog(this@HolidayActivity)
        dialogs.setContentView(R.layout.dialog_logout)
        val yesBtn = dialogs.findViewById(R.id.lay_logout_s) as RelativeLayout
        val noBtn = dialogs.findViewById(R.id.lay_cancel) as RelativeLayout
        yesBtn.setOnClickListener {
            val prefs = getSharedPreferences(ExceptionReportFragment.MY_PREFS_NAME, Context.MODE_PRIVATE)
            val edit = prefs.edit()
            edit.clear()
            // currentLocationMarker!!.position == null
            val a = Intent(applicationContext, SplashScreen::class.java)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            a.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(a)
            finish()
            dialogs.dismiss()
        }
        noBtn.setOnClickListener { dialogs.dismiss() }
        dialogs.show()
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445
    }
}
