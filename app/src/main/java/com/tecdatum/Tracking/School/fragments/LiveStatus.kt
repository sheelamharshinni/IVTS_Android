package com.tecdatum.Tracking.School.fragments

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.tecdatum.Tracking.School.R
import com.tecdatum.Tracking.School.newConstants.Url_new
import com.tecdatum.Tracking.School.newactivities.Connectivity
import com.tecdatum.Tracking.School.newactivities.SplashScreen
import com.tecdatum.Tracking.School.volley.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.*

class LiveStatus : Fragment(), OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener {
    var speed: String? = null
    var loc: String? = null
    var statuscolor: String? = null
    var direction: String? = null
    var ignition: String? = null
    var liveedate: String? = null
    var vehicletype: String? = null
    var DriverName: String? = null
    var MobNo: String? = null
    var VehicleId: String? = null
    var VehicleName: String? = null
    var lat: String? = null
    var lng: String? = null
    var sincefrom: String? = null
    private var mParam1: String? = null
    private var mParam2: String? = null
    var viewd: View? = null
    var VehicleID: String? = null
    var timer = Timer()
    var period = 1500
    var googleMapp: GoogleMap? = null
    var map: MapView? = null
    private var mLocationRequest: LocationRequest? = null
    protected var mGoogleApiClient: GoogleApiClient? = null
    protected var mLastLocation: Location? = null
    var lattitude: Double? = null
    var Lonitude: Double? = null
    var marker: Marker? = null
    var MarkerPoints = ArrayList<LatLng>()
    var sydney: LatLng? = null
    private val LiveTracking = Url_new.LiveTracking
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewd = inflater.inflate(R.layout.fragment_livestatus_info, container, false)
        map = viewd!!.findViewById(R.id.list_item_map_view_mapview)
        if (map != null) {
            map!!.onCreate(null)
            map!!.onResume()
            map!!.getMapAsync(this)
        }
        val b = arguments
        val bb = context!!.getSharedPreferences(SplashScreen.MY_PREFS_NAME, Context.MODE_PRIVATE)
        if (bb != null) {
            VehicleID = bb.getString("VehicleID", "")
            try {
                Log.e("cehckLta", "")
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        val connec = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        if (connec != null && (connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED ||
                                        connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED)) {
                            LiveTracking(VehicleID)
                        } else if (connec != null && (connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.DISCONNECTED ||
                                        connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.DISCONNECTED)) {
                            val fm = activity!!.supportFragmentManager
                            val td = Connectivity()
                            td.show(fm, "NO CONNECTION")
                        }
                    }
                }, 1, period.toLong())
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return viewd
    }

    private fun LiveTracking(VehId: String?) {
        try {
            val bb = context!!.getSharedPreferences(SplashScreen.MY_PREFS_NAME, Context.MODE_PRIVATE)
            val SPassword = bb.getString("PassWord", "")
            val SIMEI = bb.getString("IMEI", "")
            val SUserid = bb.getString("Userid", "")
            val jsonBody = JSONObject()
            jsonBody.put("UserId", SUserid)
            jsonBody.put("Password", SPassword)
            jsonBody.put("IMEI", SIMEI)
            jsonBody.put("VehicleId", "" + VehId)
            val mRequestBody = jsonBody.toString()
            Log.i("VOLLEYLIVESTATUS", mRequestBody)
            val stringRequest: StringRequest = object : StringRequest(Method.POST, LiveTracking, Response.Listener { response ->
                Log.i("VOLLEYLIVESTATUS", response)
                try {
                    val `object` = JSONObject(response)
                    val code = `object`.optString("Code").toString().toInt()
                    val message = `object`.optString("Message").toString()
                    if (googleMapp != null) {
                        googleMapp!!.clear()
                    }
                    if (marker != null) {
                        marker!!.remove()
                    }
                    MarkerPoints.clear()
                    if (code == 0) {
                        val jArray = `object`.getJSONArray("LivevehicleDetails")
                        val number = jArray.length()
                        val num = Integer.toString(number)
                        for (i in 0 until number) {
                            val json_data = jArray.getJSONObject(i)
                            VehicleName = json_data.getString("VehicleName").toString()
                            speed = json_data.getString("Speed").toString()
                            lat = json_data.getString("Lattitude").toString()
                            lng = json_data.getString("Longitude").toString()
                            loc = json_data.getString("Location").toString()
                            statuscolor = json_data.getString("fromStatus").toString()
                            direction = json_data.getString("Direction").toString()
                            sincefrom = json_data.getString("SinceFrom").toString()
                            ignition = json_data.getString("Ignition").toString()
                            liveedate = json_data.getString("LiveDate").toString()
                            vehicletype = json_data.getString("VehicleType").toString()
                            DriverName = json_data.getString("DriverName").toString()
                            MobNo = json_data.getString("MobileNo").toString()
                            VehicleId = json_data.getString("VehicleId").toString()
                            lattitude = lat.toString().toDouble()
                            Lonitude = lng.toString().toDouble()
                        }
                        sydney = LatLng(lattitude!!, Lonitude!!)
                        MarkerPoints.add(sydney!!)
                        if (MarkerPoints.size > 0) {
                            for (j in MarkerPoints.indices) {
                                if (statuscolor == "Run") {
                                    if (marker != null) {
                                        marker!!.remove()
                                    }
                                    if (vehicletype.equals("Blue Colts", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("M/C", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Car", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_running)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Patrol Mobiles", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_running)))
                                        marker!!.hideInfoWindow()
                                    }
                                }
                                if ((statuscolor == "stop") or (statuscolor == "Stop")) {
                                    if (marker != null) {
                                        marker!!.remove()
                                    }
                                    if (vehicletype.equals("M/C", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_stop)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Blue Colts", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_stop)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Car", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Patrol Mobiles", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop)))
                                        marker!!.hideInfoWindow()
                                    }
                                }
                                if (statuscolor == "Idle") {
                                    if (marker != null) {
                                        marker!!.remove()
                                    }
                                    if (vehicletype.equals("M/C", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_idle)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Blue Colts", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_idle)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Car", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_idle)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Patrol Mobiles", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_idle)))
                                        marker!!.hideInfoWindow()
                                    }
                                }
                                if ((statuscolor == "NoSignal") or (statuscolor == "Nosignal") or (statuscolor == "Nosignals")) {
                                    if (marker != null) {
                                        marker!!.remove()
                                    }
                                    if (vehicletype.equals("M/C", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Blue Colts", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Car", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Patrol Mobiles", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)))
                                        marker!!.hideInfoWindow()
                                    }
                                }
                                if ((statuscolor == "") or (statuscolor == "")) {
                                    if (marker != null) {
                                        marker!!.remove()
                                    }
                                    if (vehicletype.equals("M/C", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Blue Colts", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Car", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)))
                                        marker!!.hideInfoWindow()
                                    }
                                    if (vehicletype.equals("Patrol Mobiles", ignoreCase = true)) {
                                        marker = googleMapp!!.addMarker(MarkerOptions()
                                                .position(MarkerPoints[j]).title(VehicleName)
                                                .snippet(vehicletype).draggable(false)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)))
                                        marker!!.hideInfoWindow()
                                    }
                                }
                            }
                        }
                        val builder = LatLngBounds.Builder()
                        for (latLng in MarkerPoints) {
                            builder.include(sydney)
                            builder.include(latLng)
                        }
                        val cameraPosition = CameraPosition.Builder().target(sydney).zoom(10f).build()
                        googleMapp!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                        // googleMapp.addMarker(new MarkerOptions().position(sydney).title("Vehicle Information"));
                        googleMapp!!.setInfoWindowAdapter(object : InfoWindowAdapter {
                            override fun getInfoWindow(arg0: Marker): View? {
                                return null
                            }

                            override fun getInfoContents(arg0: Marker): View {
                                val v = layoutInflater.inflate(R.layout.info, null)
                                val t1 = "<font color='#0000FF'>  Vehicle :</font>"
                                val t2 = "<font color='#0000FF'>  Vehicle Type :</font>"
                                val t4 = "<font color='#0000FF'>  Direction :</font>"
                                val t5 = "<font color='#0000FF'>  Current Location :</font>"
                                val t6 = "<font color='#0000FF'>  Ignition :</font>"
                                val t7 = "<font color='#0000FF'>  Current Date And Time :</font>"
                                val t8 = "<font color='#0000FF'>  Speed(KMPH) :</font>"
                                val t9 = "<font color='#0000FF'>  Driver Name :</font>"
                                val t10 = "<font color='#0000FF'>  Mobile No :</font>"
                                val t11 = "<font color='#0000FF'>  Since From :</font>"
                                val t12 = "<font color='#0000FF'>  Vehicle Status:</font>"
                                val tvV = v.findViewById<View>(R.id.tv_vehicle) as TextView
                                if (VehicleName != null) {
                                    tvV.text = Html.fromHtml(t1 + VehicleName)
                                }
                                val tvVT = v.findViewById<View>(R.id.tv_vehicletype) as TextView
                                if (vehicletype != null) {
                                    tvVT.text = Html.fromHtml(t2 + vehicletype)
                                }
                                val tvsts = v.findViewById<View>(R.id.tv_vehiclestatus) as TextView
                                if (statuscolor != null) {
                                    tvsts.text = Html.fromHtml(t12 + statuscolor)
                                }
                                val tvDt = v.findViewById<View>(R.id.tv_dateandtime) as TextView
                                if (sincefrom != null) {
                                    tvDt.text = Html.fromHtml(t11 + sincefrom)
                                }
                                val tvLoc = v.findViewById<View>(R.id.tv_location) as TextView
                                if (loc != null) {
                                    tvLoc.text = Html.fromHtml(t5 + loc)
                                }
                                val tvSped = v.findViewById<View>(R.id.tv_speed) as TextView
                                if (speed != null) {
                                    tvSped.text = Html.fromHtml(t8 + speed)
                                }
                                val tvign = v.findViewById<View>(R.id.tv_ignition) as TextView
                                if (ignition != null) {
                                    tvign.text = Html.fromHtml(t6 + ignition)
                                }
                                val tvdirction = v.findViewById<View>(R.id.tv_direction) as TextView
                                if (direction != null) {
                                    tvdirction.text = Html.fromHtml(t4 + direction)
                                }
                                val tvsfm = v.findViewById<View>(R.id.tv_livedate) as TextView
                                if (liveedate != null) {
                                    tvsfm.text = Html.fromHtml(t7 + liveedate)
                                }
                                val tvdn = v.findViewById<View>(R.id.tv_drivername) as TextView
                                if (DriverName != null) {
                                    tvdn.text = Html.fromHtml(t9 + DriverName)
                                }
                                val tvmbno = v.findViewById<View>(R.id.tv_mobno) as TextView
                                if (MobNo != null) {
                                    tvmbno.text = Html.fromHtml(t10 + MobNo)
                                }
                                return v
                            }
                        })
                    } else {
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> Log.e("VOLLEY", error.toString()) }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray? {
                    return try {
                        mRequestBody?.toByteArray(charset("utf-8"))
                    } catch (uee: UnsupportedEncodingException) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8")
                        null
                    }
                } //                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//
//                        responseString = String.valueOf(response);
//
//                        // can get more details such as response.headers
//                    }
//
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//
            }
            VolleySingleton.getInstance().requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapp = googleMap
        googleMapp = googleMap
        googleMapp!!.uiSettings.isCompassEnabled = true
        googleMapp!!.uiSettings.isMyLocationButtonEnabled = true
        googleMapp!!.uiSettings.isRotateGesturesEnabled = true
        googleMapp!!.uiSettings.isMapToolbarEnabled = false
        googleMapp!!.uiSettings.isZoomControlsEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
                googleMap.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            googleMap.isMyLocationEnabled = true
        }
        googleMapp!!.animateCamera(CameraUpdateFactory.zoomTo(5f))
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // TODO: Consider calling
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
            return
        }
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // TODO: Consider calling
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
            return
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        googleMapp!!.isMyLocationEnabled = true
        if (mLastLocation != null) {
            lattitude = mLastLocation!!.latitude
            Lonitude = mLastLocation!!.longitude
            val p_lat = lattitude.toString()
            val p_longi = Lonitude.toString()
            Log.d(ContentValues.TAG, "activity locationlatlong$lattitude$Lonitude")
            Log.d(ContentValues.TAG, "String locationlatlong$p_lat$p_longi")
            val ll = LatLng(lattitude!!, Lonitude!!)
            marker = googleMapp!!.addMarker(MarkerOptions()
                    .position(ll).title("Current Location")
                    .draggable(true).icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
            marker!!.hideInfoWindow()
            //makeJsonObjReq(p_lat, p_longi,"2");
        } else {
            Log.d(ContentValues.TAG, "No Latlong found")
        }
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient!!.connect()
    }

    fun FineLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(
                                    Manifest.permission.ACCESS_FINE_LOCATION)) { //settingsDialog();
                        Toast.makeText(context, "Grant Permission for Accessing your location and provide best search results", Toast.LENGTH_LONG).show()
                        Log.d(ContentValues.TAG, "Grant Permission for Accessing your location and provide best search results")
                        // Snackbar.make(findViewById(android.R.id.content), "Grant Permission for Accessing your location and provide best search results", Snackbar.LENGTH_INDEFINITE).show();
                    }
                }
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        READ_CONTACTS_PERMISSIONS_REQUEST)
            } else {
                settingsDialog()
            }
        } else {
            settingsDialog()
        }
    }

    private fun settingsDialog() {
        buildGoogleApiClient()
        mLocationRequest = LocationRequest()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.interval = UPDATE_INTERVAL.toLong()
        mLocationRequest!!.fastestInterval = UPDATE_INTERVAL.toLong()
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest!!)
        builder.setAlwaysShow(true) //this is the key ingredient
        val result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            val States = result.locationSettingsStates
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(
                            activity,
                            REQUEST_CHECK_SETTINGS)
                } catch (e: SendIntentException) {
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val REQUEST_CHECK_SETTINGS = 0x01
        private const val READ_CONTACTS_PERMISSIONS_REQUEST = 50
        private const val UPDATE_INTERVAL = 60000
        fun newInstance(param1: String?, param2: String?): LiveStatus {
            val fragment = LiveStatus()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}