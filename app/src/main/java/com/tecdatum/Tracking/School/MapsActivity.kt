package com.tecdatum.Tracking.School

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tecdatum.Tracking.School.Constants.remote.IGoogleApi
import com.tecdatum.Tracking.School.fragments.ExceptionReportFragment
import com.tecdatum.Tracking.School.newConstants.Url_new
import com.tecdatum.Tracking.School.newactivities.Edit_ParentDetails
import com.tecdatum.Tracking.School.newactivities.HolidayActivity
import com.tecdatum.Tracking.School.newactivities.PArentHistoryTrackingDetails
import com.tecdatum.Tracking.School.newactivities.SplashScreen
import com.tecdatum.Tracking.School.newhelpers.Vehiclepoints
import com.tecdatum.Tracking.School.volley.VolleySingleton
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custommapview_pslocations.view.*
import kotlinx.android.synthetic.main.custommapview_vechilepoints.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        var markerPlaces = ArrayList<String>()
        var markerPlaces1 = ArrayList<String>()
        var wayPoints = "";
    }

    var currentLocationMarker: Marker? = null
    var currentLocationMarker_Source: Marker? = null

    var currentLocationMarker_Destination: Marker? = null

    var googleMap: GoogleMap? = null
    private val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445
    private val PointsMaster = Url_new.PointsMaster

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private var currentLocation: Location? = null
    private var firstTimeFlag = true
    private var VehicleId: String? = null
    private var apiInterface: IGoogleApi? = null
    var lat = 17.3850
    var lng = 78.4867
    var circle: Circle? = null;
    var lattitude: Double? = null
    var Lonitude: Double? = null
    var Lalangs: LatLng? = null
    var PointName: String? = null
    var line: Polyline? = null;
    var timer = Timer()
    var VehicleName: String? = null
    var speed: String? = null
    var loc: String? = null
    var MobNo: String? = null
    var statuscolor: String? = null
    var direction: String? = null
    var sincefrom: String? = null
    var ignition: String? = null
    var liveedate: String? = null
    var vehicletype: String? = null
    var DriverName: String? = null
    var UserName: String? = null
    var VechileId: String? = null
    var VehicleNumber: String? = null
    var Live_Longitude: String? = null
    var Live_Latitude: String? = null
    var RouteName: String? = null
    var RouteID: String? = null
    var StartTime: String? = null
    var EndTime: String? = null

    var PointLatitude: String? = null
    var PointLongitude: String? = null
    var Droplat: String? = null
    var Droplang: String? = null

    var supportMapFragment: SupportMapFragment? = null

    var viapoints: ArrayList<Vehiclepoints>? = ArrayList<Vehiclepoints>()

    var viapoints_latlangs: ArrayList<Vehiclepoints>? = ArrayList<Vehiclepoints>()
    var center: Location? = null;
    var test: Location? = null;

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult.lastLocation == null) return
            currentLocation = locationResult.lastLocation
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation)
                firstTimeFlag = false
            }
            showMarker(currentLocation)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.supportMap) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)
        statusCheck()


        val bb = getSharedPreferences(SplashScreen.MY_PREFS_NAME, Context.MODE_PRIVATE)
        StartTime = bb.getString("StartTime", "")
        EndTime = bb.getString("EndTime", "")
        UserName = bb.getString("ParentName", "")
        VehicleNumber = bb.getString("VehicleNumber", "")
        RouteName = bb.getString("RouteName", "")
        RouteID = bb.getString("RouteID", "")

        Live_Latitude = bb.getString("Live_Latitude", "")
        Live_Longitude = bb.getString("Live_Longitude", "")


        Droplat = bb.getString("PointLatitude", "")
        Droplang = bb.getString("PointLongitude", "")


        if (UserName != null) {
            if (UserName != "") {
                textView.text = "Welcome $UserName"
            } else {
                textView.visibility = View.GONE
            }
        } else {
            textView.visibility = View.GONE
        }

        lay_routeandvechile.visibility = View.VISIBLE
        lay_durationtime.visibility = View.VISIBLE
        lay_back.visibility = View.INVISIBLE
        lay_back.isEnabled = false

        txt_VehicleNumber.text = VehicleNumber
        text_RouteName.text = RouteName


        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)




        try {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    val connec = this@MapsActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if ((connec != null && (((connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() === NetworkInfo.State.CONNECTED) || (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() === NetworkInfo.State.CONNECTED))))) {

                        val c = Calendar.getInstance()
                        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)


                        if (timeOfDay < 11) {

                            RouteID?.let { getpickpoints("PickPoints", it) }

                        } else if (timeOfDay >= 11) {

                            RouteID?.let { getpickpoints("DropPoints", it) }


                        }
                        var isBetween = EndTime!!.toInt() > StartTime!!.toInt() && timeOfDay >= StartTime!!.toInt() && timeOfDay <= EndTime!!.toInt() || EndTime!!.toInt() < StartTime!!.toInt() && timeOfDay >= StartTime!!.toInt() || timeOfDay <= EndTime!!.toInt()

                        if (timeOfDay >= Integer.valueOf(StartTime!!) && timeOfDay < Integer.valueOf(EndTime!!)) { //checkes whether the current time is between 14:49:00 and 20:11:13.
                            println(true)
                            try {
                                getDirection(lat, lng, PointLatitude!!.toDouble(), PointLongitude!!.toDouble())
                                getDirection_distance(lat, lng, Droplat!!.toDouble(), Droplang!!.toDouble())
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            timer.cancel()
                            val i = Intent(applicationContext, HolidayActivity::class.java)
                            startActivity(i)
                        }

                    } else if ((connec != null && (((connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() === NetworkInfo.State.DISCONNECTED) || (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() === NetworkInfo.State.DISCONNECTED))))) {

                    }
                }
            }, 1, 1800000)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        try {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    val connec = this@MapsActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if ((connec != null && (((connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() === NetworkInfo.State.CONNECTED) || (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() === NetworkInfo.State.CONNECTED))))) {
                        service()
                        val c = Calendar.getInstance()
                        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)


                        if (timeOfDay >= Integer.valueOf(StartTime!!) && timeOfDay < Integer.valueOf(EndTime!!)) { //checkes whether the current time is between 14:49:00 and 20:11:13.
                            println(true)
                            try {
                                getDirection(lat, lng, PointLatitude!!.toDouble(), PointLongitude!!.toDouble())
                                getDirection_distance(lat, lng, Droplat!!.toDouble(), Droplang!!.toDouble())


                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            timer.cancel()
                            val i = Intent(applicationContext, HolidayActivity::class.java)
                            startActivity(i)
                        }


                    } else if ((connec != null && (((connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() === NetworkInfo.State.DISCONNECTED) || (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() === NetworkInfo.State.DISCONNECTED))))) {

                    }
                }
            }, 1, 15000)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        if (timeOfDay >= Integer.valueOf(StartTime!!) && timeOfDay < Integer.valueOf(EndTime!!)) { //checkes whether the current time is between 14:49:00 and 20:11:13.
            println(true)
            try {
                getDirection(lat, lng, PointLatitude!!.toDouble(), PointLongitude!!.toDouble())
                getDirection_distance(lat, lng, Droplat!!.toDouble(), Droplang!!.toDouble())

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            val i = Intent(applicationContext, HolidayActivity::class.java)
            startActivity(i)
        }


        navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.navigation_Profile -> {
                    val intent = Intent(this@MapsActivity, Edit_ParentDetails::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_Logout -> {
                    logoutdialog()

                    true


                }
                R.id.navigation_history -> {
                    timer.cancel()
                    val intent = Intent(this@MapsActivity, PArentHistoryTrackingDetails::class.java)
                    startActivity(intent)
                    true


                }
                else -> false
            }
        }
    }

    override fun onMapReady(googleMapp: GoogleMap?) {
        googleMap = googleMapp
        googleMap!!.uiSettings.isZoomControlsEnabled = true;


    }

    private fun startCurrentLocationUpdates() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 3000
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
                return
            }
        }
        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper())
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (ConnectionResult.SUCCESS == status) return true else {
            if (googleApiAvailability.isUserResolvableError(status)) Toast.makeText(this, "Please Install google play services to use this application", Toast.LENGTH_LONG).show()
        }
        return false
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
                                        VehicleId = route.getString("VehicleId").toString()


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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
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
    private fun getCameraPositionWithBearing(latLng: LatLng): CameraPosition? {
        return CameraPosition.Builder().target(latLng).zoom(16f).build()
    }

    private fun showMarker(currentLocation: Location?) {

        val Title: String = VehicleName + "_" + statuscolor + "_" + ignition + "_" + sincefrom + "_" +
                speed + "_" + liveedate + "_" + DriverName + "_" + MobNo + "_" + loc


        val latLng = LatLng(lat, lng)

        if (currentLocation != null)
            if (currentLocationMarker == null)
                currentLocationMarker = googleMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_fortracking)).position(latLng).title(Title))
            else
                MarkerAnimation.animateMarkerToGB(currentLocationMarker!!, latLng, LatLngInterpolator.Spherical(), googleMap!!)

        val customInfoWindow = CustomInfoWindow(layoutInflater)
        googleMap!!.setInfoWindowAdapter(customInfoWindow)
        markerPlaces1.add(currentLocationMarker!!.getId())

    }

    private fun getDirection(fromLatitude: Double, fromLongitude: Double, toLatitude: Double, toLongitude: Double) {
        viapoints!!.clear()
        val url: String = makeURL(fromLatitude, fromLongitude, toLatitude, toLongitude)


        var stringRequest = StringRequest(url, com.android.volley.Response.Listener<String>() { response ->

            drawPath(response)
        }, com.android.volley.Response.ErrorListener { error -> Log.e("VOLLEY", error.toString()) })

        Log.e("STRINGREQUEST", "" + stringRequest)
        stringRequest.retryPolicy = DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        VolleySingleton.getInstance().requestQueue.add(stringRequest)

    }

    private fun getDirection_distance(fromLatitude: Double, fromLongitude: Double, toLatitude: Double, toLongitude: Double) {
        viapoints!!.clear()
        val url: String = makeURLdistance(fromLatitude, fromLongitude, toLatitude, toLongitude)


        var stringRequest = StringRequest(url, com.android.volley.Response.Listener<String>() { response ->

            drawPathdistance(response)
        }, com.android.volley.Response.ErrorListener { error -> Log.e("VOLLEY", error.toString()) })

        Log.e("STRINGREDIATCNCE", "" + stringRequest)
        stringRequest.retryPolicy = DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        VolleySingleton.getInstance().requestQueue.add(stringRequest)

    }

    fun drawPath(result: String?) {
        if (line != null) {
            line!!.remove()
        }
        if (circle != null) {
            circle!!.remove()
        }


        val Title: String = VehicleName + "_" + PointName
        currentLocationMarker_Destination = googleMap!!.addMarker(MarkerOptions()
                .position(LatLng(Droplat!!.toDouble(), Droplang!!.toDouble())).title(Title)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_allpoints)))
        val customInfoWindow = CustomInfoWindow(layoutInflater)
        googleMap!!.setInfoWindowAdapter(customInfoWindow)
        markerPlaces.add(currentLocationMarker_Destination!!.getId())
        currentLocationMarker_Destination!!.showInfoWindow()

//        circle = googleMap!!.addCircle(CircleOptions().center(LatLng(lat.toDouble(), lng.toDouble())).strokeWidth(1f).radius(10000.0).strokeColor(Color.RED));

        try { //Parsing json
            val json = JSONObject(result)
            val routeArray = json.getJSONArray("routes")
            val routes = routeArray.getJSONObject(0)
            val legs = routes.getJSONArray("legs")
            val steps = legs.getJSONObject(0)
            val distance = steps.getJSONObject("distance")
            val distancetext = distance.getString("text")
            val duration = steps.getJSONObject("duration")
            val durationtext = duration.getString("text")


            val overviewPolylines = routes.getJSONObject("overview_polyline")
            val encodedString = overviewPolylines.getString("points")
            val list: List<LatLng> = decodePoly(encodedString)
            line = googleMap!!.addPolyline(PolylineOptions()
                    .addAll(list)
                    .width(5f)
                    .color(Color.BLACK)
                    .geodesic(true))

        } catch (e: JSONException) {
        }


    }

    fun makeURL(sourcelat: Double, sourcelog: Double, destlat: Double, destlog: Double): String {
        viapoints!!.clear()
        val urlString = StringBuilder()
        urlString.append("https://maps.googleapis.com/maps/api/directions/json")
        urlString.append("?origin=") // from
        urlString.append(java.lang.Double.toString(sourcelat))
        urlString.append(",")


        urlString.append(java.lang.Double.toString(sourcelog))


        urlString.append("&destination=") // to
        //urlString.append(Destinationlatlang)
        urlString.append(java.lang.Double.toString(destlat))
        urlString.append(",")
        urlString.append(java.lang.Double.toString(destlog))

        urlString.append("&key=AIzaSyCL6a5z3GH088gmKurSnhSMzofVyuLmlvY")
        urlString.append("&sensor=false&mode=driving&alternatives=true")
        urlString.append("&waypoints=") // to


        urlString.append(wayPoints)



        return urlString.toString()
    }

    fun drawPathdistance(result: String?) {


        try { //Parsing json
            val json = JSONObject(result)
            val routeArray = json.getJSONArray("routes")
            val routes = routeArray.getJSONObject(0)
            val legs = routes.getJSONArray("legs")
            val steps = legs.getJSONObject(0)
            val distance = steps.getJSONObject("distance")
            val distancetext = distance.getString("text")
            val duration = steps.getJSONObject("duration")
            val durationtext = duration.getString("text")


            tv_Distance.text = distancetext
            tv_Duration.text = durationtext


        } catch (e: JSONException) {
        }


    }

    fun makeURLdistance(sourcelat: Double, sourcelog: Double, destlat: Double, destlog: Double): String {
        val urlString = StringBuilder()
        urlString.append("https://maps.googleapis.com/maps/api/directions/json")
        urlString.append("?origin=") // from
        urlString.append(java.lang.Double.toString(sourcelat))
        urlString.append(",")


        urlString.append(java.lang.Double.toString(sourcelog))


        urlString.append("&destination=") // to
        //urlString.append(Destinationlatlang)
        urlString.append(java.lang.Double.toString(destlat))
        urlString.append(",")
        urlString.append(java.lang.Double.toString(destlog))

        urlString.append("&key=AIzaSyCL6a5z3GH088gmKurSnhSMzofVyuLmlvY")
        urlString.append("&sensor=false&mode=driving&alternatives=true")



        return urlString.toString()
    }

    override fun onStop() {
        timer.cancel()
        super.onStop()
        if (fusedLocationProviderClient != null) fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume() {

        super.onResume()
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            startCurrentLocationUpdates()
        }
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
        fusedLocationProviderClient = null
        // googleMap = null
    }

    fun logoutdialog() {
        val dialogs = Dialog(this@MapsActivity)
        dialogs.setContentView(R.layout.dialog_logout)
        val yesBtn = dialogs.findViewById(R.id.lay_logout_s) as RelativeLayout
        val noBtn = dialogs.findViewById(R.id.lay_cancel) as RelativeLayout
        yesBtn.setOnClickListener {
            wayPoints = ""
            viapoints!!.clear()
            timer.cancel()
            googleMap!!.clear()
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

    object MarkerAnimation {
        var isMarkerRotating = false
        internal var lat = 0.0
        internal var lng = 0.04
        fun animateMarkerToGB(marker: Marker, finalPosition: LatLng, latLngInterpolator: LatLngInterpolator, googleMap: GoogleMap) {
            // if (marker != null) {
            // marker.remove();
            // }
//            if (!isMarkerRotating) {
            val startPosition = marker.getPosition()
            val handler = Handler()
            val start = SystemClock.uptimeMillis()
            val interpolator = AccelerateDecelerateInterpolator()
            val durationInMs = 2000f
            handler.post(object : Runnable {
                internal var elapsed: Long = 0
                internal var t: Float = 0.toFloat()
                internal var v: Float = 0.toFloat()
                public override fun run() {
                    isMarkerRotating = true
                    // Ca lculate progress using interpolator
                    elapsed = SystemClock.uptimeMillis() - start
                    t = elapsed / durationInMs
                    v = interpolator.getInterpolation(t)

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(finalPosition).zoom(16f).build()))
                    // Repeat till progress is complete.
                    if (startPosition.equals(finalPosition)) {
                        marker.isVisible = true


                    } else {
                        marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition))
                        marker.setRotation(getBearing(startPosition, finalPosition))

                        //marker.setPosition(finalPosition);
                    }



                    if (t < 1) {
                        // Post again 10ms later.
                        handler.postDelayed(this, 10)
                    } else {
                        isMarkerRotating = false
                    }
                }
            })
            //}
        }

        fun getBearing(begin: LatLng, end: LatLng): Float {
            lat = Math.abs(begin.latitude - end.latitude)
            lng = Math.abs(begin.longitude - end.longitude)
            // Log.e("LATLANGVALUES","END"+end);
            if (begin.latitude < end.latitude && begin.longitude < end.longitude)
                return (Math.toDegrees(Math.atan(lng / lat))).toFloat()
            else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
                return ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90).toFloat()
            else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
                return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
            else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
                return ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270).toFloat()
            return -1f
        }
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    private fun getpickpoints(sp_pickup_Name: String, pointId: String) {
        var sp_pickup_Name: String? = sp_pickup_Name
        var pointId: String? = pointId
        val jsonBody = JSONObject()
        if (sp_pickup_Name == null) {
            sp_pickup_Name = ""
        }
        if (pointId == null) {
            pointId = ""
        }
        try {
            jsonBody.put("ActionName", "" + sp_pickup_Name)
            jsonBody.put("RouteID", "" + pointId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val mRequestBody = jsonBody.toString()
        Log.e("VOLLEY", "PointsMaster$mRequestBody")
        val stringRequest: StringRequest = object : StringRequest(Method.POST, PointsMaster, com.android.volley.Response.Listener { response ->
            Log.e("VOLLEY", "PointsMaster$response")
            try {
                val `object` = JSONObject(response)
                val code = `object`.optString("Code")
                viapoints!!.clear()
                val message = `object`.optString("Message")
                if (code.equals("0", ignoreCase = true)) {
                    val jArray = `object`.getJSONArray("PointData")
                    val number = jArray.length()
                    val num = Integer.toString(number)
                    if (number == 0) {
                        Toast.makeText(this@MapsActivity, " No Data Found", Toast.LENGTH_LONG).show()
                    } else {
                        for (i in 0 until number) {
                            val json_data = jArray.getJSONObject(i)
                            val PointID = json_data.getString("PointID")
                            val TypeOfPointsID = json_data.getString("TypeOfPointsID")
                            val PointType = json_data.getString("PointType")
                            val PointCode = json_data.getString("PointCode")

                            PointName = json_data.getString("PointName")
                            val Location = json_data.getString("Location")
                            val Latitude = json_data.getString("Latitude")
                            val Longitude = json_data.getString("Longitude")

                            lattitude = Latitude.toString().toDouble()
                            Lonitude = Longitude.toString().toDouble()


                            val wp1 = Vehiclepoints(Latitude, Longitude, PointName!!)
                            viapoints!!.add(wp1)


                        }

                        var foundIt: Boolean = false;
                        var i = 0
                        while (i < viapoints!!.size - 1) {
//                            if (viapoints!![i].latitude == Drop_Latitude.toString()) {
//                                foundIt = true
//                                break
//                            }
                            i++
                            val pointt = viapoints!!.get(i);
                            val Title: String = VehicleNumber + "_" + pointt.pointName
                            val customInfoWindow = CustomInfoWindow(layoutInflater)
                            googleMap!!.setInfoWindowAdapter(customInfoWindow)


//                            currentLocationMarker_Source = googleMap!!.addMarker(MarkerOptions()
//                                    .position(LatLng(pointt.latitude.toDouble(), pointt.longitude.toDouble())).title(Title)
//                                    .snippet(VehicleName)
//                                    .draggable(false)
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_allpoints)))


//                            if (Droplat!!.equals(viapoints!!.get(i))) {
//                                currentLocationMarker_Source!!.remove()
//                            }
//                            markerPlaces.add(currentLocationMarker_Source!!.getId())
                            wayPoints += pointt.latitude + "," + pointt.longitude + "|";
                            try {
                                PointLatitude = viapoints!!.get(i - 1).latitude
                                PointLongitude = viapoints!!.get(i - 1).longitude


                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }


                            val wp1 = Vehiclepoints(pointt.latitude, pointt.longitude, PointName!!)
                            viapoints_latlangs!!.add(wp1)


//                            Lalangs = currentLocationMarker_Source!!.position

                        }
                        try {
                            googleMap!!.addMarker(MarkerOptions()
                                    .position(LatLng(viapoints!!.get(viapoints!!.size - 1).latitude!!.toDouble(), viapoints!!.get(viapoints!!.size - 1).longitude!!.toDouble())).title(PointName)
                                    .draggable(false)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_destination)))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                } else {

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, com.android.volley.Response.ErrorListener { error -> Log.e("VOLLEY", error.toString()) }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return mRequestBody?.toByteArray(StandardCharsets.UTF_8)
            }
        }
        VolleySingleton.getInstance().requestQueue.add(stringRequest)
    }

    class CustomInfoWindow(inflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {
        internal var inflater: LayoutInflater? = null


        init {
            this.inflater = inflater
        }

        override fun getInfoWindow(marker: Marker): View? {
            if (marker != null) {
                if (markerPlaces.containsAll(Collections.singleton(marker.getId()))) {
                    val view1 = inflater!!.inflate(R.layout.custommapview_vechilepoints, null)
                    // Getting the position from the marker
                    val latLng = marker.getPosition()
                    val title = marker.getTitle()
                    val str2 = title.split(("_").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    try {
                        view1.tv_heirarchynamme.setText(str2[0])
                        view1.tv_ps.setText(str2[1])
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    return (view1)

                } else if (markerPlaces1.containsAll(Collections.singleton(marker.getId()))) {
                    val v: View = inflater!!.inflate(R.layout.custommapview_pslocations, null)
                    val title1 = marker.title
                    val str2 = title1.split("_").toTypedArray()

                    v.tv_vehiclenumber.text = str2[0]
                    v.tv_vehiclestatus.text = str2[1]
                    v.tv_ignition.text = str2[2]
                    v.tv_Sincefrom.text = str2[3]
                    v.tv_speed.text = str2[4]
                    v.tv_currentDateandtime.text = str2[5]
                    v.tv_DriverName.text = str2[6]
                    v.tv_drivermobile.text = str2[7]
                    v.tv_Location.text = str2[8]





                    return v
                }

            }
            return null
        }


        override fun getInfoContents(marker: Marker): View? {
            return null
        }
    }
}







