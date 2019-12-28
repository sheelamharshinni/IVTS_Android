package com.tecdatum.Tracking.School.newactivities

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tecdatum.Tracking.School.DateTime.CustomDateTimePicker
import com.tecdatum.Tracking.School.R
import com.tecdatum.Tracking.School.newConstants.Url_new
import com.tecdatum.Tracking.School.newConstants.Url_new.PointsMaster
import com.tecdatum.Tracking.School.newactivities.SplashScreen.MY_PREFS_NAME
import com.tecdatum.Tracking.School.volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_historytracking.*
import kotlinx.android.synthetic.main.custommapview_vechilepoints.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PArentHistoryTrackingDetails : AppCompatActivity(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var currentLocationMarker: Marker? = null
    private var currentLocation: Location? = null
    private var firstTimeFlag = true
    var VehicleName: String? = null
    var speed: String? = null
    var vehicletype: String? = null
    var StartTime: String? = null
    var EndTime: String? = null
    var VechileId: String? = null
    var line: Polyline? = null
    var lat = 17.3850
    var lng = 78.4867
    var ParentMobile: String? = null
    var UserName: String? = null
    var DisplayName: String? = null
    var StudentID: String? = null
    var StudentClass: String? = null
    var ParentName: String? = null
    private val HistoryTracking = Url_new.HistoryTracking
    var pDialog: ProgressDialog? = null
    var PointName: String? = null
    var currentLocationMarker_Source: Marker? = null
    var CCtvMarkers: ArrayList<Marker> = ArrayList()
    private val url1 = Url_new.LiveTracking
    var a_VehicleNo: String? = null
    var message: String? = null
    var vehicle: String? = null
    var default_From_date: String? = null
    var default_To_date: String? = null
    var Lo: String? = null
    var DT: String? = null
    var Dire: String? = null
    var Ign: String? = null
    var vehicleT: String? = null
    var Start_time: String? = null
    var Stop_time: String? = null
    var default_mode: String? = null
    var latt = 17.3850
    var lngg = 78.4867
    var position: LatLng? = null
    var Locatn_time = ArrayList<String>()
    var Points = ArrayList<LatLng>()
    var custom: CustomDateTimePicker? = null
    var custom1: CustomDateTimePicker? = null
    var date_fm: Calendar? = null
    var date_to: Calendar? = null
    var RouteID: String? = null
    var timeOfDay: Int = 0
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult.getLastLocation() == null) return
            currentLocation = locationResult.getLastLocation()
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation)
                firstTimeFlag = false
            }
        }
    }

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historytracking)
        statusCheck()
        val supportMapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.supportMap) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)
        val bb: SharedPreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        StartTime = bb.getString("StartTime", "")
        EndTime = bb.getString("EndTime", "")
        ParentMobile = bb.getString("ParentMobile", "")
        UserName = bb.getString("UserName", "")
        StudentID = bb.getString("StudentID", "")
        DisplayName = bb.getString("DisplayName", "")
        ParentName = bb.getString("ParentName", "")
        StudentClass = bb.getString("StudentClass", "")

        RouteID = bb.getString("RouteID", "")
        lay_back.setOnClickListener {
            finish()
        }
        VechileId = bb.getString("VehicleID", "")
        HistoryTracking(VechileId!!)

        select.setOnClickListener {
            fromdata()
        }
        val c = Calendar.getInstance()
        timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        if (cb_points.isChecked == true) {

            if (timeOfDay < 11) {

                RouteID?.let { getpickpoints("PickPoints", it) }

            } else if (timeOfDay >= 11) {

                RouteID?.let { getpickpoints("DropPoints", it) }


            }

        } else {
            hideCCtvs()
        }
        cb_points.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            val c = Calendar.getInstance()
            val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

            if (b) {

                if (timeOfDay < 11) {

                    RouteID?.let { getpickpoints("PickPoints", it) }

                } else if (timeOfDay >= 11) {

                    RouteID?.let { getpickpoints("DropPoints", it) }


                }

            } else {
                hideCCtvs()
            }
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap!!.uiSettings.isZoomControlsEnabled = true
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

                val message = `object`.optString("Message")
                if (code.equals("0", ignoreCase = true)) {
                    val jArray = `object`.getJSONArray("PointData")
                    val number = jArray.length()
                    val num = Integer.toString(number)
                    if (number == 0) {
                        Toast.makeText(this@PArentHistoryTrackingDetails, " No Data Found", Toast.LENGTH_LONG).show()
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

                            val Title: String = "" + "_" + PointName
                            currentLocationMarker_Source = googleMap!!.addMarker(MarkerOptions()
                                    .position(LatLng(Latitude.toDouble(), Longitude.toDouble())).title(Title)
                                    .snippet(VehicleName)
                                    .draggable(false)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_allpoints)))


                            val customInfoWindow = CustomInfoWindow(layoutInflater)
                            googleMap!!.setInfoWindowAdapter(customInfoWindow)
                            CCtvMarkers.add(currentLocationMarker_Source!!)
                            markerPlaces.add(currentLocationMarker_Source!!.getId())
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

    private fun startCurrentLocationUpdates() {
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(3000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@PArentHistoryTrackingDetails, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
        val latLng = LatLng(latt, lngg)
        googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)))
    }


    @NonNull
    private fun getCameraPositionWithBearing(latLng: LatLng): CameraPosition {
        return CameraPosition.Builder().target(latLng).zoom(16F).build()
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

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445
        var markerPlaces = ArrayList<String>()
    }


    private fun hideCCtvs() {
        for (marker in CCtvMarkers) {
            marker.remove()
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
    private fun HistoryTracking1(vehicleid: String, fromdate: String, todate: String) {
        pDialog = ProgressDialog(this@PArentHistoryTrackingDetails)
        pDialog!!.setMessage("Please Wait While Redirect..")
        pDialog!!.setIndeterminate(false)
        pDialog!!.setCancelable(true)
        pDialog!!.show()
        val bb: SharedPreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        VechileId = bb.getString("VehicleID", "")

        try {
            val jsonBody = JSONObject()

            jsonBody.put("Mode", default_mode)
            jsonBody.put("VehicleId", vehicleid)
            jsonBody.put("StartDate", fromdate)
            jsonBody.put("EndDate", todate)
            jsonBody.put("Duration", "5")
            val mRequestBody = jsonBody.toString()
            Log.e("HistoryTracking", "request HistoryTracking$mRequestBody")
            val stringRequest: StringRequest = object : StringRequest(Method.POST, HistoryTracking, Response.Listener { response ->
                Log.i("HistoryTracking", "Response HistoryTracking$response")
                try {
                    val `object` = JSONObject(response)
                    val code = `object`.optString("Code").toString()
                    if (code.equals("0", ignoreCase = true)) { //  Track_info.setVisibility(View.VISIBLE);
                        val jArray = `object`.getJSONArray("HistoryData")
                        val number = jArray.length()
                        val num = Integer.toString(number)
                        Log.i("history lat", num)
                        if (number == 0) {
                        } else {
                            if (Points != null) {
                                Points.clear()
                            }
                            if (googleMap != null) {
                                googleMap!!.clear()
                            }
                            for (i in 0 until number) {
                                val json_data = jArray.getJSONObject(i)
                                a_VehicleNo = json_data.getString("Vehicle").toString()
                                lat = json_data.getString("Latitude").toDouble()
                                lng = json_data.getString("Longitude").toDouble()
                                Lo = json_data.getString("Location").toString()
                                DT = json_data.getString("Datetime").toString()
                                Dire = json_data.getString("Speed").toString()
                                Ign = json_data.getString("Ignition").toString()
                                vehicleT = json_data.getString("VehicleType").toString()
                                latt = lat!!.toDouble()
                                lngg = lng!!.toDouble()
                                position = LatLng(latt, lngg)
                                Locatn_time.add("" + DT)
                                Points.add(position!!)
                                googleMap!!.addMarker(MarkerOptions()
                                        .position(position!!).title(vehicle)
                                        .snippet(Lo).icon(BitmapDescriptorFactory.fromResource(R.drawable.points)))
                            }
                            pDialog!!.dismiss()

                            drawPolyLineOnMap(Points)
                            Log.d("history la", Points.toString())
                            googleMap!!.addMarker(MarkerOptions()
                                    .position(Points.get(0)).title("Start")
                                    .snippet(Locatn_time.get(0)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start)))
                            googleMap!!.addMarker(MarkerOptions()
                                    .position(Points.get(Points.size - 1)).title("End")
                                    .snippet(Locatn_time.get(Points.size - 1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_stop)))
                            val c = Calendar.getInstance()
                            timeOfDay = c.get(Calendar.HOUR_OF_DAY)
                            if (timeOfDay < 11) {

                                RouteID?.let { getpickpoints("PickPoints", it) }

                            } else if (timeOfDay >= 11) {

                                RouteID?.let { getpickpoints("DropPoints", it) }


                            }
                        }
                    } else {
                        googleMap!!.clear()
                        pDialog!!.dismiss()

                        var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@PArentHistoryTrackingDetails)
                        alertDialog.setTitle("Alert")
                        alertDialog.setMessage("" + "No Data Found")
                        alertDialog.setIcon(R.drawable.alert)
                        alertDialog.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                        alertDialog.show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                Log.e("VOLLEY", error.toString())
                pDialog!!.dismiss()
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @RequiresApi(Build.VERSION_CODES.KITKAT)
                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return mRequestBody?.toByteArray(StandardCharsets.UTF_8)
                } //
            }
            stringRequest.retryPolicy = DefaultRetryPolicy(
                    25000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            VolleySingleton.getInstance().getRequestQueue().add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun HistoryTracking(vehicleid: String) {
        pDialog = ProgressDialog(this@PArentHistoryTrackingDetails)
        pDialog!!.setMessage("Please Wait While Redirect..")
        pDialog!!.setIndeterminate(false)
        pDialog!!.setCancelable(true)
        pDialog!!.show()
        val bb: SharedPreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        VechileId = bb.getString("VehicleID", "")
        setdate()
        try {
            val jsonBody = JSONObject()

            jsonBody.put("Mode", default_mode)
            jsonBody.put("VehicleId", vehicleid)
            jsonBody.put("StartDate", default_From_date)
            jsonBody.put("EndDate", default_To_date)
            jsonBody.put("Duration", "5")
            val mRequestBody = jsonBody.toString()
            Log.e("HistoryTracking", "request HistoryTracking$mRequestBody")
            val stringRequest: StringRequest = object : StringRequest(Method.POST, HistoryTracking, Response.Listener { response ->
                Log.i("HistoryTracking", "Response HistoryTracking$response")
                try {
                    val `object` = JSONObject(response)
                    val code = `object`.optString("Code").toString()
                    if (code.equals("0", ignoreCase = true)) { //  Track_info.setVisibility(View.VISIBLE);
                        val jArray = `object`.getJSONArray("HistoryData")
                        val number = jArray.length()
                        val num = Integer.toString(number)
                        Log.i("history lat", num)
                        if (number == 0) {
                        } else {
                            if (Points != null) {
                                Points.clear()
                            }
                            if (googleMap != null) {
                                googleMap!!.clear()
                            }
                            for (i in 0 until number) {
                                val json_data = jArray.getJSONObject(i)
                                a_VehicleNo = json_data.getString("Vehicle").toString()
                                lat = json_data.getString("Latitude").toDouble()
                                lng = json_data.getString("Longitude").toDouble()
                                Lo = json_data.getString("Location").toString()
                                DT = json_data.getString("Datetime").toString()
                                Dire = json_data.getString("Speed").toString()
                                Ign = json_data.getString("Ignition").toString()
                                vehicleT = json_data.getString("VehicleType").toString()
                                latt = lat!!.toDouble()
                                lngg = lng!!.toDouble()
                                position = LatLng(latt, lngg)
                                Locatn_time.add("" + DT)
                                Points.add(position!!)
                                googleMap!!.addMarker(MarkerOptions()
                                        .position(position!!).title(vehicle)
                                        .snippet(Lo).icon(BitmapDescriptorFactory.fromResource(R.drawable.points)))
                            }
                            pDialog!!.dismiss()

                            drawPolyLineOnMap(Points)
                            Log.d("history la", Points.toString())
                            googleMap!!.addMarker(MarkerOptions()
                                    .position(Points.get(0)).title("Start")
                                    .snippet(Locatn_time.get(0)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start)))
                            googleMap!!.addMarker(MarkerOptions()
                                    .position(Points.get(Points.size - 1)).title("End")
                                    .snippet(Locatn_time.get(Points.size - 1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_stop)))
                            val c = Calendar.getInstance()
                            timeOfDay = c.get(Calendar.HOUR_OF_DAY)
                            if (timeOfDay < 11) {

                                RouteID?.let { getpickpoints("PickPoints", it) }

                            } else if (timeOfDay >= 11) {

                                RouteID?.let { getpickpoints("DropPoints", it) }


                            }
                        }
                    } else {
                        googleMap!!.clear()
                        pDialog!!.dismiss()

                        var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@PArentHistoryTrackingDetails)
                        alertDialog.setTitle("Alert")
                        alertDialog.setMessage("" + "No Data Found")
                        alertDialog.setIcon(R.drawable.alert)
                        alertDialog.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                        alertDialog.show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                Log.e("VOLLEY", error.toString())
                pDialog!!.dismiss()
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @RequiresApi(Build.VERSION_CODES.KITKAT)
                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return mRequestBody?.toByteArray(StandardCharsets.UTF_8)
                } //
            }
            stringRequest.retryPolicy = DefaultRetryPolicy(
                    25000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            VolleySingleton.getInstance().getRequestQueue().add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }



    fun statusCheck() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
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

    @SuppressLint("SimpleDateFormat")
    private fun fromdata() {

        // Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
        val alertDialog = android.app.AlertDialog.Builder(this@PArentHistoryTrackingDetails).create()
        //  alertDialog.setTitle("Select Date and time");
        //  alertDialog.setTitle("Select Date and time");
        val inflater = this@PArentHistoryTrackingDetails.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_datetime, null)
        alertDialog.setView(dialogView)
        val tv = TextView(this@PArentHistoryTrackingDetails)
        val fm = EditText(this@PArentHistoryTrackingDetails)
        val toT = EditText(this@PArentHistoryTrackingDetails)
        val spnr = Spinner(this@PArentHistoryTrackingDetails)
        val spnr1 = Spinner(this@PArentHistoryTrackingDetails)
        fm.inputType = InputType.TYPE_NULL
        toT.inputType = InputType.TYPE_NULL
        tv.text = "Select Date"
        // tv.setGravity(0);
        tv.gravity = Gravity.CENTER
        tv.textSize = 19f
        tv.setTextColor(Color.BLACK)
        fm.setText(default_From_date)
        toT.setText(default_To_date)


        Start_time = fm.text.toString()
        Stop_time = toT.text.toString()
        Log.e("Start_time", Start_time)
        Log.e("Stop_time", Stop_time)

        custom = CustomDateTimePicker(this@PArentHistoryTrackingDetails,
                object : CustomDateTimePicker.ICustomDateTimeListener {
                    override fun onSet(dialog: Dialog?, calendarSelected: Calendar,
                                       dateSelected: Date?, year: Int, monthFullName: String?,
                                       monthShortName: String?, monthNumber: Int, date: Int,
                                       weekDayFullName: String?, weekDayShortName: String?,
                                       hour24: Int, hour12: Int, min: Int, sec: Int,
                                       AM_PM: String?) { //
//                        Toast.makeText(getApplicationContext(), "" + year
//                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                + " " + hour24 + ":" + min
//                                + ":" + sec, Toast.LENGTH_LONG).show();
//                        ((TextInputEditText) findViewById(R.id.edtEventDateTime))
                        fm.setText("")
                        fm.setText(year
                                .toString() + "-" + (monthNumber + 1) + "-" + calendarSelected[Calendar.DAY_OF_MONTH]
                                + " " + hour24 + ":" + min
                        )
                    }

                    override fun onCancel() {}
                })
        custom!!.set24HourFormat(true)
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom!!.setDate(Calendar.getInstance())
        custom1 = CustomDateTimePicker(this@PArentHistoryTrackingDetails,
                object : CustomDateTimePicker.ICustomDateTimeListener {
                    @SuppressLint("SetTextI18n")
                    override fun onSet(dialog: Dialog?, calendarSelected: Calendar,
                                       dateSelected: Date?, year: Int, monthFullName: String?,
                                       monthShortName: String?, monthNumber: Int, date: Int,
                                       weekDayFullName: String?, weekDayShortName: String?,
                                       hour24: Int, hour12: Int, min: Int, sec: Int,
                                       AM_PM: String?) { //
//                        Toast.makeText(this, "" + year
//                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                + " " + hour24 + ":" + min
//                                + ":" + sec, Toast.LENGTH_LONG).show();
//                        ((TextInputEditText) findViewById(R.id.edtEventDateTime))
                        toT.setText("")
                        toT.setText(year
                                .toString() + "-" + (monthNumber + 1) + "-" + calendarSelected[Calendar.DAY_OF_MONTH]
                                + " " + hour24 + ":" + min
                        )
                    }

                    override fun onCancel() {}
                })
        custom1!!.set24HourFormat(true)
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom1!!.setDate(Calendar.getInstance())
        fm.setOnClickListener {
            //custom.showDialog();
            val currentDate = Calendar.getInstance()
            date_fm = Calendar.getInstance()
            val DatePickerDialog1 = DatePickerDialog(this@PArentHistoryTrackingDetails,
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // view.setMaxDate(System.currentTimeMillis());
                        view.maxDate = currentDate.timeInMillis
                        date_fm!!.set(year, monthOfYear, dayOfMonth)
                        TimePickerDialog(this@PArentHistoryTrackingDetails, OnTimeSetListener { view, hourOfDay, minute ->
                            date_fm!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            date_fm!!.set(Calendar.MINUTE, minute)
                            val df = SimpleDateFormat("yyyy-MM-dd hh:mm")
                            fm.setText("")
                            fm.setText("" + df!!.format(date_fm!!.getTime()))
                        }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false).show()
                    }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE])
            DatePickerDialog1.datePicker.maxDate = System.currentTimeMillis()
            DatePickerDialog1.show()
        }
        toT.setOnClickListener {
            // custom1.showDialog();
            val currentDate = Calendar.getInstance()
            date_to = Calendar.getInstance()
            val DatePickerDialog2 = DatePickerDialog(this@PArentHistoryTrackingDetails, OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date_to!!.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(this@PArentHistoryTrackingDetails, OnTimeSetListener { view, hourOfDay, minute ->
                    date_to!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    date_to!!.set(Calendar.MINUTE, minute)
                    val df = SimpleDateFormat("yyyy-MM-dd hh:mm")
                    toT.setText("")
                    toT.setText("" + df.format(date_to!!.getTime()))
                }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false).show()
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE])
            DatePickerDialog2.datePicker.maxDate = System.currentTimeMillis()
            DatePickerDialog2.show()
        }
        //        fm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        toT.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        val ll = LinearLayout(this@PArentHistoryTrackingDetails)
        ll.orientation = LinearLayout.VERTICAL
        ll.addView(tv)
        ll.addView(fm)
        ll.addView(toT)
        //ll.addView(spnr);
//        ll.addView(spnr1);
        alertDialog!!.setView(ll)
        alertDialog!!.setButton(AlertDialog.BUTTON_POSITIVE, "Submit"
        ) { dialog, which ->
            default_From_date = fm.text.toString()
            default_To_date = toT.text.toString()
            Log.e("changed_from", default_From_date)
            Log.e("changed_to", default_To_date)
            Log.e("changed_mode", default_mode)
            //  makeJsonObjReq();
            HistoryTracking1(VechileId!!, default_From_date!!, default_To_date!!)

            dialog.dismiss()
        }
        alertDialog!!.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }


    fun drawPolyLineOnMap(list: List<LatLng?>) {
        Log.e("histry", list.toString())
//
//        polyOptions = PolylineOptions()
//        polyOptions!!.color(Color.BLUE)
//        polyOptions!!.width(5f)
//        polyOptions!!.addAll(list)
//        polyOptions!!.geodesic(true)
//        googleMap!!.addPolyline(polyOptions)

        line = googleMap!!.addPolyline(PolylineOptions()
                .addAll(list)
                .width(5f)
                .color(Color.BLUE)
                .geodesic(true))

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

                }

            }
            return null
        }


        override fun getInfoContents(marker: Marker): View? {
            return null
        }
    }


}
