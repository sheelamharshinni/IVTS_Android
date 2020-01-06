package com.tecdatum.Tracking.School.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
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
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.tecdatum.Tracking.School.R
import com.tecdatum.Tracking.School.newConstants.Url_new
import com.tecdatum.Tracking.School.newactivities.SplashScreen
import com.tecdatum.Tracking.School.volley.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*

class HistoryTrackingFragment : Fragment(), OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener {
    var a_VehicleNo: String? = null
    var lat: String? = null
    var lng: String? = null
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
    var latt = 0.0
    var lngg = 0.0
    var position: LatLng? = null
    var polyOptions: PolylineOptions? = null
    var vehicle: String? = null
    var default_From_date: String? = null
    var default_To_date: String? = null
    var message: String? = null
    var Lo: String? = null
    var DT: String? = null
    var Dire: String? = null
    var Ign: String? = null
    var vehicleT: String? = null
    var default_mode: String? = null
    private val HistoryTracking = Url_new.HistoryTracking
    var Points: ArrayList<LatLng>? = ArrayList()
    var Locatn_time = ArrayList<String>()
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
            HistoryTracking(VehicleID)
        }
        return viewd
    }

    private fun HistoryTracking(veh_id: String?) { //   makeJsonObjReq_Vehiclelist();
        val bb = context!!.getSharedPreferences(SplashScreen.MY_PREFS_NAME, Context.MODE_PRIVATE)
        val SPassword = bb.getString("PassWord", "")
        val SBranchid = bb.getString("Branchid", "")
        val SIMEI = bb.getString("IMEI", "")
        val SUserid = bb.getString("Userid", "")
        setdate()
        try {
            Log.d("Vehicle_Name", veh_id)
            val jsonBody = JSONObject()
            //   SUsername= bb.getString("Username", "");
//   Toast.makeText(getApplicationContext(), " Result" +SOrgid+SUserid, Toast.LENGTH_LONG).show();
            jsonBody.put("Userid", SUserid)
            jsonBody.put("Password", SPassword)
            jsonBody.put("Imei", SIMEI)
            jsonBody.put("Mode", default_mode)
            jsonBody.put("VehicleId", veh_id)
            jsonBody.put("StartDate", default_From_date)
            jsonBody.put("EndDate", default_To_date)
            jsonBody.put("Duration", "5")
            val mRequestBody = jsonBody.toString()
            Log.e(ContentValues.TAG, "request HistoryTracking$mRequestBody")
            val stringRequest: StringRequest = object : StringRequest(Method.POST, HistoryTracking, Response.Listener { response ->
                Log.i(ContentValues.TAG, "Response HistoryTracking$response")
                try {
                    val `object` = JSONObject(response)
                    val code = `object`.optString("Code").toString()
                    val message = `object`.optString("Message").toString()
                    if (code.equals("0", ignoreCase = true)) { //  Track_info.setVisibility(View.VISIBLE);
                        val jArray = `object`.getJSONArray("HistoryData")
                        val number = jArray.length()
                        val num = Integer.toString(number)
                        Log.i("history lat", num)
                        if (number == 0) { // Toast.makeText(getContext(), " No Data Found", Toast.LENGTH_LONG).show();
                        } else { //                                alert.setVisibility(View.GONE);
//                                alertmessage.setVisibility(View.GONE);
// googleMap.clear();
                            googleMapp!!.clear()
                            if (Points != null) {
                                Points!!.clear()
                            }
                            //                                removeAllValuesInDb();
                            for (i in 0 until number) {
                                val json_data = jArray.getJSONObject(i)
                                a_VehicleNo = json_data.getString("Vehicle").toString()
                                lat = json_data.getString("Latitude").toString()
                                lng = json_data.getString("Longitude").toString()
                                Lo = json_data.getString("Location").toString()
                                DT = json_data.getString("Datetime").toString()
                                Dire = json_data.getString("Speed").toString()
                                Ign = json_data.getString("Ignition").toString()
                                vehicleT = json_data.getString("VehicleType").toString()
                                latt = java.lang.Double.valueOf(lat!!)
                                lngg = java.lang.Double.valueOf(lng!!)
                                position = LatLng(latt, lngg)
                                Locatn_time.add("" + DT)
                                Points!!.add(position!!)
                                googleMapp!!.addMarker(MarkerOptions()
                                        .position(position!!).title(vehicle)
                                        .snippet(Lo).icon(BitmapDescriptorFactory.fromResource(R.drawable.points)))
                                //  .icon(BitmapDescriptorFactory.fromResource(R.drawable.points))).showInfoWindow();
                            }
                            showOrHideInfoWindows(false)
                            drawPolyLineOnMap(Points)
                            Log.i("history la", Points.toString())
                            googleMapp!!.addMarker(MarkerOptions()
                                    .position(Points!![0]).title("Start")
                                    .snippet(Locatn_time[0]).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start)))
                            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.strtbtn))).showInfoWindow();
                            googleMapp!!.addMarker(MarkerOptions()
                                    .position(Points!![Points!!.size - 1]).title("End")
                                    .snippet(Locatn_time[Points!!.size - 1]).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_stop)))
                            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.stopbtn))).showInfoWindow();
                        }
                    } else {
                        googleMapp!!.clear()
                        //  Track_info.setVisibility(View.GONE);
                        val alertDialog = AlertDialog.Builder(context)
                        alertDialog.setTitle("Alert")
                        alertDialog.setMessage("" + message)
                        alertDialog.setIcon(R.drawable.alert)
                        alertDialog.setPositiveButton("Ok") { dialog, which ->
                            buildGoogleApiClient()
                            marker!!.hideInfoWindow()
                            dialog.cancel()
                        }
                        alertDialog.show()
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
            stringRequest.retryPolicy = DefaultRetryPolicy(
                    25000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            VolleySingleton.getInstance().requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun showOrHideInfoWindows(shouldShow: Boolean) {
        if (shouldShow) marker!!.showInfoWindow() else marker!!.hideInfoWindow()
    }

    fun drawPolyLineOnMap(list: List<LatLng>?) {
        Log.e("histry", list.toString())
        //       for (int z = 0; z < list.size() - 1; z++) {
// LatLng src = list.get(z);
// LatLng dest = list.get(z + 1);
// line = mMap.addPolyline(new PolylineOptions()
// .add(new LatLng(src.latitude, src.longitude),
// new LatLng(dest.latitude, dest.longitude))
// .width(5).color(Color.RED).geodesic(true));
//
// }
        polyOptions = PolylineOptions()
        polyOptions!!.color(Color.BLUE)
        polyOptions!!.width(5f)
        polyOptions!!.addAll(list)
        polyOptions!!.geodesic(true)
        googleMapp!!.addPolyline(polyOptions)
        //    pDialog.dismiss();
        val builder = LatLngBounds.Builder()
        for (latLng in list!!) {
            builder.include(latLng)
        }
        val bounds = builder.build()
        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 80)
        googleMapp!!.animateCamera(cu)
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

    private fun setdate() {
        val simpledateformat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val from_date = Date()
        val to_date = from_date.time - 1440 * 60 * 1000
        Log.e("TEST", from_date.time.toString() + " - " + to_date)
        default_From_date = simpledateformat.format(to_date)
        default_To_date = simpledateformat.format(from_date)
        default_mode = "Both"
        Log.e("from", default_From_date)
        Log.e("to", default_To_date)
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
        fun newInstance(param1: String?, param2: String?): HistoryTrackingFragment {
            val fragment = HistoryTrackingFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}