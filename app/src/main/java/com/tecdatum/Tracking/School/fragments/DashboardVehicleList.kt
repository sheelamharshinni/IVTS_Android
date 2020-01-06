/*
package com.tecdatum.Tracking.School.fragments

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayout
import com.tecdatum.Tracking.School.R
import com.tecdatum.Tracking.School.newConstants.Url_new
import com.tecdatum.Tracking.School.newhelpers.QvVehiclesList
import com.tecdatum.Tracking.School.volley.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DashboardVehicleList : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    var v: View? = null
    var rg_runing: RadioButton? = null
    var rg_stop: RadioButton? = null
    var rg_idle: RadioButton? = null
    var rg_nosignal: RadioButton? = null
    var daily_weekly_button_view: RadioGroup? = null
    var UserID: String? = null
    var SPassword: String? = null
    var SIMEI: String? = null
    var SOrgid: String? = null
    var SBranchid: String? = null
    var SUserid: String? = null
    var Orgid: String? = null
    var LevelId: String? = null
    var HierarchyId: String? = null
    var VehicleTpe: String? = null
    var Vehiclestatus: String? = null
    private val DashboardVehicleList1 = Url_new.DashboardVehicleList
    var arraylist = ArrayList<QvVehiclesList>()
    var a_VehicleNo: String? = null
    var a_StartDateTime: String? = null
    var a_StartLocation: String? = null
    var a_EndDateTime: String? = null
    var a_EndLocation: String? = null
    var a_Distance: String? = null
    var a_TravelTime: String? = null
    var a_IdleTime: String? = null
    var a_StopTime: String? = null
    var a_Total_Distance: String? = null
    var adapter: HeroAdapter? = null
    private val DashBoardChart = Url_new.DashBoardChart
    private val HistoryTrackInformation = Url_new.HistoryTrackInformation
    private val url_LiveTracking = Url_new.LiveTracking
    private var exampleListFull: List<QvVehiclesList>? = null
    private var mParam1: String? = null
    private var mParam2: String? = null
    var xVals = ArrayList<String>()
    var yVals = ArrayList<Entry>()
    var arraylist_Time = ArrayList<String>()
    var arraylist_Distance = ArrayList<String>()
    var arraylist_Distance1 = ArrayList<Int>()
    var arraylist_Time1 = ArrayList<Int>()
    var default_From_date: String? = null
    var default_To_date: String? = null
    var message: String? = null
    var default_mode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_dashboard_vehiclelist, container, false)
        recyclerView = v!!.findViewById<View>(R.id.my_recycler_view) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        val tv_fragmentName = v!!.findViewById<View>(R.id.tv_fragmentName) as TextView
        tv_fragmentName.text = "Vehicle List"
        rg_runing = v!!.findViewById(R.id.rg_runing)
        rg_stop = v!!.findViewById(R.id.rg_stop)
        rg_idle = v!!.findViewById(R.id.rg_idle)
        rg_nosignal = v!!.findViewById(R.id.rg_nosignal)
        daily_weekly_button_view = v!!.findViewById(R.id.daily_weekly_button_view)
        val b = arguments
        if (b != null) {
            VehicleTpe = b.getString("TYPE")
            Vehiclestatus = b.getString("STATUS")
            Log.d("VEHICLETYPESTATUS", VehicleTpe)
            if (Vehiclestatus != null) {
                if (!Vehiclestatus!!.isEmpty()) {
                    if (Vehiclestatus == "Run") {
                        rg_runing!!.setChecked(true)
                        if (rg_runing!!.isChecked()) {
                            makeJsonObjReq(Vehiclestatus!!, VehicleTpe)
                        } else {
                        }
                    } else if (Vehiclestatus == "Stop") {
                        rg_stop!!.setChecked(true)
                        if (rg_stop!!.isChecked()) {
                            makeJsonObjReq(Vehiclestatus!!, VehicleTpe)
                        } else {
                        }
                    } else if (Vehiclestatus == "Idle") {
                        rg_idle!!.setChecked(true)
                        if (rg_idle!!.isChecked()) {
                            makeJsonObjReq(Vehiclestatus!!, VehicleTpe)
                        } else {
                        }
                    } else if (Vehiclestatus == "Nosignal") {
                        rg_nosignal!!.setChecked(true)
                        if (rg_nosignal!!.isChecked()) {
                            makeJsonObjReq(Vehiclestatus!!, VehicleTpe)
                        } else {
                        }
                    }
                }
            }
        } else {
            Vehiclestatus = "Run"
            VehicleTpe = "Car"
            rg_runing!!.setChecked(true)
        }
        if (rg_runing!!.isChecked()) {
            makeJsonObjReq("Run", "Car")
        }
        daily_weekly_button_view!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val selectedId = daily_weekly_button_view!!.getCheckedRadioButtonId()
            val radiolngButton = v!!.findViewById<View>(selectedId) as RadioButton
            if (radiolngButton.text.toString().equals("Running", ignoreCase = true)) {
                Vehiclestatus = "Run"
                makeJsonObjReq(Vehiclestatus!!, VehicleTpe)
            }
            if (radiolngButton.text.toString().equals("Stop", ignoreCase = true)) {
                Vehiclestatus = "Stop"
                makeJsonObjReq(Vehiclestatus!!, VehicleTpe)
            }
            if (radiolngButton.text.toString().equals("Idle", ignoreCase = true)) {
                Vehiclestatus = "Idle"
                makeJsonObjReq(Vehiclestatus!!, VehicleTpe)
            }
            if (radiolngButton.text.toString().equals("In Repair", ignoreCase = true)) {
                Vehiclestatus = "Nosignal"
                makeJsonObjReq(Vehiclestatus!!, VehicleTpe)
            }
        })
        return v
    }

    private fun makeJsonObjReq(status: String, VehicleTyp: String?) {
        try {
            val bb = context!!.getSharedPreferences(MonthDayReport_Fragment.MY_PREFS_NAME, Context.MODE_PRIVATE)
            UserID = bb.getString("UserID", "")
            Orgid = bb.getString("Orgid", "")
            LevelId = bb.getString("LevelId", "")
            HierarchyId = bb.getString("HierarchyId", "")
            val jsonBody = JSONObject()
            jsonBody.put("OrgId", Orgid)
            jsonBody.put("VehicleNo", "")
            jsonBody.put("UserId", UserID)
            jsonBody.put("VehicleType", VehicleTyp)
            jsonBody.put("Fromstatus", status)
            jsonBody.put("LevelId", LevelId)
            jsonBody.put("HierarchyId", HierarchyId)
            val mRequestBody = jsonBody.toString()
            Log.i("VOLLEY", mRequestBody)
            val stringRequest: StringRequest = object : StringRequest(Method.POST, DashboardVehicleList1, Response.Listener { response ->
                Log.i("VOLLEY", response)
                try {
                    val `object` = JSONObject(response)
                    val code = `object`.optString("Code").toString().toInt()
                    val message = `object`.optString("Message").toString()
                    if (code == 0) { //    progressDialog.dismiss();
                        recyclerView!!.visibility = View.VISIBLE
                        val jArray = `object`.getJSONArray("VehicleList")
                        val number = jArray.length()
                        val num = Integer.toString(number)
                        if (number == 0) {
                            Toast.makeText(context, " No Data Found", Toast.LENGTH_LONG).show()
                        } else {
                            arraylist.clear()
                            for (i in 0 until number) {
                                val json_data = jArray.getJSONObject(i)
                                val vehicle_Id = json_data.getString("Id").toString()
                                val VehicleRegNo = json_data.getString("VehicleRegNo").toString()
                                val Vehicle = json_data.getString("Vehicle").toString()
                                val Vehicletype = json_data.getString("Vehicletype").toString()
                                var Sincefrom = json_data.getString("Sincefrom").toString()
                                val Livedatetime = json_data.getString("Livedatetime").toString()
                                val DriverName = json_data.getString("DriverName").toString()
                                val MobileNo = json_data.getString("MobileNo").toString()
                                val VehicleStatus = json_data.getString("VehicleStatus").toString()
                                val Location = json_data.getString("Location").toString()
                                val Latitude = json_data.getString("Latitude").toString()
                                val Longitude = json_data.getString("Longitude").toString()
                                val AcStatus = json_data.getString("AcStatus").toString()
                                val Gpsstatus = json_data.getString("Gpsstatus").toString()
                                val Speed = json_data.getString("Speed").toString()
                                val Ignition = json_data.getString("Ignition").toString()
                                val Model = json_data.getString("Vehicletype").toString()
                                val date = Sincefrom
                                try {
                                    val inputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy hh:mma")
                                    val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy hh:mm")
                                    val oneWayTripDate = inputFormat.parse(date)
                                    Sincefrom = outputFormat.format(oneWayTripDate) // parse input
                                    //holder.tv_DateofInspection.setText(s);
                                } catch (ex: Exception) {
                                    println("Date error")
                                }
                                val wp = QvVehiclesList(vehicle_Id, VehicleRegNo, Vehicle, Vehicletype, Sincefrom,
                                        Livedatetime, DriverName, MobileNo, VehicleStatus, Location, Latitude, Longitude, AcStatus, Gpsstatus, Speed, Ignition, Model)
                                // Binds all strings into an array
                                arraylist.add(wp)
                            }
                            adapter = HeroAdapter(arraylist)
                            recyclerView!!.adapter = adapter
                        }
                    } else { //  progressDialog.dismiss();
                        try {
                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setTitle("Response")
                            alertDialog.setMessage("" + message)
                            alertDialog.setPositiveButton("Ok") { dialog, which ->
                                // Write your code here to invoke YES event
                                dialog.cancel()
                            }
                            alertDialog.show()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                Log.e("VOLLEY", error.toString())
                arraylist.clear()
                recyclerView!!.visibility = View.GONE
                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_LONG).show()
            }) {
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
                }
            }
            stringRequest.retryPolicy = DefaultRetryPolicy(
                    5010000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            VolleySingleton.getInstance().requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class HeroAdapter(private val heroList: List<QvVehiclesList>) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>(), Filterable {
        private var context: Context? = null
        private var currentPosition = 0
        var isclickoverviewlay = true
        override fun getFilter(): Filter? {
            return null
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout_adapter, parent, false)
            return HeroViewHolder(v)
        }

        override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
            val hero = heroList[position]
            val hero1 = exampleListFull!![position]
            holder.bind(hero, position)
            holder.textViewName.text = hero.vehicle
            holder.textViewVersion.text = hero.location
            holder.sincefrom.text = hero.sincefrom


            if (hero.vehicleStatus == "Run") {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true) or (hero.vehicletype == "M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_run)
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_running)
                }
            }
            if ((hero.vehicleStatus == "Stop") or (hero.vehicleStatus == "stop")) {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true) or (hero.vehicletype == "M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_stop)
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_stop)
                }
            }
            if (hero.vehicleStatus == "Idle") {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true) or (hero.vehicletype == "M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_idle)
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_idle)
                }
            }
            if ((hero.vehicleStatus == "Nosignal") or (hero.vehicleStatus == "Nosignals") or (hero.vehicleStatus == "UnderFailure") or (hero.vehicleStatus == "NoSignal")) {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true) or (hero.vehicletype == "M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal)
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair)
                }
            }
            if ((hero.vehicleStatus == "") or (hero.vehicleStatus == "") or (hero.vehicleStatus == "")) {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true) or (hero.vehicletype == "M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal)
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair)
                }
            }
            if (hero.vehicleStatus == "Run") {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_run)
                }
                if (hero.vehicletype.equals("M/C", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_run)
                }
                if (hero.vehicletype.equals("Car", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.bus_running)
                }
            }
            if ((hero.vehicleStatus == "Stop") or (hero.vehicleStatus == "stop")) {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_stop)
                }
                if (hero.vehicletype.equals("M/C", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_stop)
                }
                if (hero.vehicletype.equals("Car", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.bus_stop)
                }
            }
            if (hero.vehicleStatus == "Idle") {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_idle)
                }
                if (hero.vehicletype.equals("M/C", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_idle)
                }
                if (hero.vehicletype.equals("Car", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.bus_idle)
                }
            }
            if ((hero.vehicleStatus == "Nosignal") or (hero.vehicleStatus == "Nosignals") or (hero.vehicleStatus == "UnderFailure") or (hero.vehicleStatus == "NoSignal")) {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal)
                }
                if (hero.vehicletype.equals("M/C", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal)
                }
                if (hero.vehicletype.equals("Car", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair)
                }
            }
            if ((hero.vehicleStatus == "") or (hero.vehicleStatus == "") or (hero.vehicleStatus == "")) {
                if (hero.vehicletype.equals("Blue Colts", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal)
                }
                if (hero.vehicletype.equals("M/C", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal)
                }
                if (hero.vehicletype.equals("Car", ignoreCase = true)) {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair)
                }
            }
            holder.card_view.setOnClickListener {
                currentPosition = position
                val expanded = hero.isExpanded
                val prefs = activity!!.getSharedPreferences(MonthDayReport_Fragment.MY_PREFS_NAME, Context.MODE_PRIVATE)
                val edit = prefs.edit()
                edit.putString("VehicleID", heroList[position].vehicle_Id)
                edit.commit()
                hero.isExpanded = !expanded
                notifyItemChanged(position)
                notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int {
            return heroList.size
        }

        inner class HeroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var vehicleid:String?=null
            var textViewName: TextView
            var textViewVersion: TextView
            var sincefrom: TextView
            var imageView: ImageView
            var card_view: CardView
            var tabLayout: TabLayout
            var viewPager: ViewPager
            var linearLayout: LinearLayout
            private val mChart: LineChart
            var tv_more: TextView
            var expand: ImageView
            var collapse: ImageView
            var ll_vehiclesadapter: LinearLayout
            var tv_estimatetime: TextView
            var tv_ql_StartDatetime: TextView
            var tv_ql_StartLocation: TextView
            var tv_ql_EndDatetime: TextView
            var tv_ql_EndLocation: TextView
            var tv_ql_Distance: TextView
            var tv_ql_TravelTime: TextView
            var tv_ql_IdleTime: TextView
            var tv_ql_StopTime: TextView
            fun bind(movie: QvVehiclesList, position: Int) {
                val expanded = movie.isExpanded
                if (position == currentPosition) {
                    vehicleid=heroList.get(position).vehicle_Id
                    val bundle=Bundle()
                    bundle.putString("vehicleid", vehicleid)
                    linearLayout.visibility = if (expanded) View.VISIBLE else View.GONE
                    tabLayout.visibility = if (expanded) View.VISIBLE else View.GONE
                    graphCall("", movie.vehicle_Id)
                    TrackingINFO(movie.vehicle_Id, movie.vehicle)
                } else {
                    linearLayout.visibility = View.GONE
                    tabLayout.visibility = View.GONE
                }
                if (position == currentPosition) {
                    if (expanded == true) {
                        expand.visibility = View.GONE
                        collapse.visibility = View.VISIBLE
                        tv_more.visibility = View.VISIBLE
                    } else {
                        expand.visibility = View.VISIBLE
                        collapse.visibility = View.GONE
                        tv_more.visibility = View.GONE
                    }
                } else {
                    expand.visibility = View.VISIBLE
                    collapse.visibility = View.GONE
                    tv_more.visibility = View.GONE
                }
            }

            private fun graphCall(Date: String, vehicleid: String) {
                mChart.setNoDataTextDescription("or" + "\n" + "Loading Chart Data")
                try {
                    val bb = getContext()!!.getSharedPreferences(MonthDayReport_Fragment.MY_PREFS_NAME, Context.MODE_PRIVATE)
                    SOrgid = bb.getString("Orgid", "")
                    SPassword = bb.getString("PassWord", "")
                    SBranchid = bb.getString("Branchid", "")
                    SIMEI = bb.getString("IMEI", "")
                    SUserid = bb.getString("Userid", "")
                    val jsonBody = JSONObject()
                    jsonBody.put("Userid", SUserid)
                    jsonBody.put("Password", SPassword)
                    jsonBody.put("Imei", SIMEI)
                    jsonBody.put("Vehicleid", vehicleid)
                    //  jsonBody.put("Date", Date);
                    val mRequestBody = jsonBody.toString()
                    Log.i("VOLLEY", "input to dashchart$mRequestBody")
                    val stringRequest: StringRequest = object : StringRequest(Method.POST, DashBoardChart, Response.Listener { response ->
                        Log.i("VOLLEY", "Output to dashchart$response")
                        try {
                            val `object` = JSONObject(response)
                            val code = `object`.optString("Code").toString()
                            val message = `object`.optString("Message").toString()
                            if (code.equals("0", ignoreCase = true)) {
                                xVals.clear()
                                yVals.clear()
                                arraylist_Time.clear()
                                arraylist_Distance.clear()
                                arraylist_Time1.clear()
                                arraylist_Distance1.clear()
                                x_axis.clear()
                                y_axis.clear()
                                a_Total_Distance = `object`.optString("TotalDistance").toString()
                                val jArray = `object`.getJSONArray("Data")
                                val number = jArray.length()
                                val num = Integer.toString(number)
                                if (number == 0) {
                                    Toast.makeText(getContext(), " No Data Found", Toast.LENGTH_LONG).show()
                                } else {
                                    try {
                                        for (i in 0 until number) {
                                            val json_data = jArray.getJSONObject(i)
                                            val Hour = json_data.getString("Hour").toString()
                                            val Distance = json_data.getString("Distance").toString()
                                            arraylist_Time.add(Hour)
                                            val distance = Distance.toFloat()
                                            val jsonFormattedString = Hour.replace(":".toRegex(), ".")
                                            val Time = jsonFormattedString.toFloat()
                                            val Hour_f = Math.round(Time)
                                            var result: Float
                                            result = round(distance, 2)
                                            val hour = Hour_f
                                            x_axis.add(Math.round(Time).toString())
                                            y_axis.add(result.toString())
                                        }
                                        Log.e("Chart y", "" + yVals.size)
                                        Log.e("Chart x", "" + xVals.size)
                                        setLineChartData(setXAxisValues(), y_axis)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            } else {
                                val alertDialog = AlertDialog.Builder(getContext())
                                alertDialog.setTitle("Response")
                                alertDialog.setMessage("" + message)
                                alertDialog.setPositiveButton("Ok") { dialog, which ->
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
                        }
                    }
                    stringRequest.retryPolicy = DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                    VolleySingleton.getInstance().requestQueue.add(stringRequest)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            private fun TrackingINFO(veh_id: String, veh_name: String) {
                setdate()
                val bb = getContext()!!.getSharedPreferences(MonthDayReport_Fragment.MY_PREFS_NAME, Context.MODE_PRIVATE)
                val ID = bb.getString("Id", "")
                val VEHICLE_NAME = bb.getString("VehicleName", "")
                val quary = bb.getString("query", "")
                SPassword = bb.getString("PassWord", "")
                SBranchid = bb.getString("Branchid", "")
                SIMEI = bb.getString("IMEI", "")
                SUserid = bb.getString("Userid", "")
                Log.i("1", quary)
                tv_estimatetime.text = "From-$default_From_date\tTo-$default_To_date"
                try {
                    Log.d("Vehicle_Name", veh_id)
                    val jsonBody = JSONObject()
                    jsonBody.put("UserId", SUserid)
                    jsonBody.put("Password", SPassword)
                    jsonBody.put("IMEI", SIMEI)
                    jsonBody.put("VehicleId", veh_id)
                    jsonBody.put("StartDate", default_From_date)
                    jsonBody.put("EndDate", default_To_date)
                    val mRequestBody = jsonBody.toString()
                    Log.e(ContentValues.TAG, "request HistoryTrackInformation$mRequestBody")
                    val stringRequest: StringRequest = object : StringRequest(Method.POST, HistoryTrackInformation, Response.Listener { response ->
                        Log.i(ContentValues.TAG, "Response HistoryTrackInformation$response")
                        try {
                            val `object` = JSONObject(response)
                            val code = `object`.optString("Code").toString()
                            message = `object`.optString("Message").toString()
                            if (code.equals("0", ignoreCase = true)) {
                                a_VehicleNo = `object`.optString("VehicleNo").toString()
                                a_StartDateTime = `object`.optString("StartDateTime").toString()
                                a_StartLocation = `object`.optString("StartLocation").toString()
                                a_EndDateTime = `object`.optString("EndDateTime").toString()
                                a_EndLocation = `object`.optString("EndLocation").toString()
                                a_TravelTime = `object`.optString("TravelTime").toString()
                                a_IdleTime = `object`.optString("IdleTime").toString()
                                a_StopTime = `object`.optString("StopTime").toString()
                                if (a_StartDateTime == null) {
                                    tv_ql_StartDatetime.text = ""
                                } else {
                                    tv_ql_StartDatetime.text = "" + a_StartDateTime
                                }
                                if (a_StartLocation == null) {
                                    tv_ql_StartLocation.text = ""
                                } else {
                                    tv_ql_StartLocation.text = "" + a_StartLocation
                                }
                                if (a_EndDateTime == null) {
                                    tv_ql_EndDatetime.text = ""
                                } else {
                                    tv_ql_EndDatetime.text = "" + a_EndDateTime
                                }
                                if (a_EndLocation == null) {
                                    tv_ql_EndLocation.text = ""
                                } else {
                                    tv_ql_EndLocation.text = "" + a_EndLocation
                                }
                                if (a_Total_Distance == null) {
                                    tv_ql_Distance.text = ""
                                } else {
                                    tv_ql_Distance.text = "" + a_Total_Distance
                                }
                                if (a_TravelTime == null) {
                                    tv_ql_TravelTime.text = ""
                                } else {
                                    tv_ql_TravelTime.text = "" + a_TravelTime
                                }
                                if (a_IdleTime == null) {
                                    tv_ql_IdleTime.text = ""
                                } else {
                                    tv_ql_IdleTime.text = "" + a_IdleTime
                                }
                                if (a_StopTime == null) {
                                    tv_ql_StopTime.text = ""
                                } else {
                                    tv_ql_StopTime.text = "" + a_StopTime
                                }
                            } else {
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }, Response.ErrorListener { error ->
                        Log.e("VOLLEY", error.toString())
                        // pDialog.dismiss();
                    }) {
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
                        }
                    }
                    stringRequest.retryPolicy = DefaultRetryPolicy(
                            5000000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                    VolleySingleton.getInstance().requestQueue.add(stringRequest)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            private fun setdate() {
                val simpledateformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val from_date = Date()
                val to_date = from_date.time - 1440 * 60 * 1000
                Log.e("TEST", from_date.time.toString() + " - " + to_date)
                default_From_date = simpledateformat.format(to_date)
                default_To_date = simpledateformat.format(from_date)
                default_mode = "Both"
                Log.e("from", default_From_date)
                Log.e("to", default_To_date)
            }

            fun round(d: Float, decimalPlace: Int): Float {
                var bd = BigDecimal(java.lang.Float.toString(d))
                bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
                return bd.toFloat()
            }

            private fun setLineChartData(xVal: ArrayList<String>, yVal: ArrayList<String>) {
                val entries1: MutableList<Entry> = ArrayList()
                for (i in xVal.indices) {
                    entries1.add(Entry(java.lang.Float.valueOf(yVal[i]), Integer.valueOf(xVal[i])))
                }
                val dataSet = LineDataSet(entries1, "Distance(km)(Y-axis) Vs Time Chart(hrs)(X-axis) ")
                Log.e("ChartData 23", "" + entries1 + "" + "\n")
                Log.e("ChartData 11", "" + xVal.size + "" + "\n" + arraylist_Time.size + "\n" + yVal.size)
                mChart.setDescription("---> Time (Hrs)")
                dataSet.color = Color.BLUE
                dataSet.color = Color.BLUE
                dataSet.setCircleColor(Color.BLUE)
                dataSet.lineWidth = 2f
                dataSet.circleRadius = 3f
                dataSet.setDrawCircleHole(true)
                val data = LineData(arraylist_Time, dataSet)
                val xAxis = mChart.xAxis
                mChart.data = data
                mChart.invalidate()
            }

            private fun setXAxisValues(): ArrayList<String> {
                xVals = ArrayList()

                xVals.add("00")
                xVals.add("1")
                xVals.add("2")
                xVals.add("3")
                xVals.add("4")
                xVals.add("5")
                xVals.add("6")
                xVals.add("7")
                xVals.add("8")
                xVals.add("9")
                xVals.add("10")
                xVals.add("11")
                xVals.add("12")
                xVals.add("13")
                xVals.add("14")
                xVals.add("15")
                xVals.add("16")
                xVals.add("17")
                xVals.add("18")
                xVals.add("19")
                xVals.add("20")
                xVals.add("21")
                xVals.add("22")
                xVals.add("23")
                xVals.add("24")
                return xVals
            }

            init {
                textViewVersion = itemView.findViewById(R.id.textViewVersion)
                textViewName = itemView.findViewById<View>(R.id.textViewName) as TextView
                sincefrom = itemView.findViewById(R.id.sincefrom)
                imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
                card_view = itemView.findViewById(R.id.card_view)
                linearLayout = itemView.findViewById(R.id.linearLayout)
                viewPager = itemView.findViewById<View>(R.id.viewpager) as ViewPager
                val viewPagerAdapter = SimpleFragmentPagerAdapter(activity!!.supportFragmentManager)
                viewPager.adapter = viewPagerAdapter
                tabLayout = itemView.findViewById<View>(R.id.sliding_tabs) as TabLayout
                tabLayout.setupWithViewPager(viewPager)
                mChart = itemView.findViewById<View>(R.id.linechart_distance) as LineChart
                tv_more = itemView.findViewById(R.id.tv_more)
                ll_vehiclesadapter = itemView.findViewById(R.id.ll_vehiclesadapter)
                expand = itemView.findViewById(R.id.expand)
                collapse = itemView.findViewById(R.id.collapse)
                tv_estimatetime = itemView.findViewById<View>(R.id.tv_estimatetime) as TextView
                tv_ql_StartDatetime = itemView.findViewById<View>(R.id.tv_ql_StartDatetime) as TextView
                tv_ql_StartLocation = itemView.findViewById<View>(R.id.tv_ql_StartLocation) as TextView
                tv_ql_EndDatetime = itemView.findViewById<View>(R.id.tv_ql_EndDatetime) as TextView
                tv_ql_EndLocation = itemView.findViewById<View>(R.id.tv_ql_EndLocation) as TextView
                tv_ql_Distance = itemView.findViewById<View>(R.id.tv_ql_Distance) as TextView
                tv_ql_TravelTime = itemView.findViewById<View>(R.id.tv_ql_TravelTime) as TextView
                tv_ql_IdleTime = itemView.findViewById<View>(R.id.tv_ql_IdleTime) as TextView
                tv_ql_StopTime = itemView.findViewById<View>(R.id.tv_ql_StopTime) as TextView
            }
        }

        init {
            exampleListFull = heroList
            context = context
            exampleListFull = ArrayList(heroList)
        }
    }

    inner class SimpleFragmentPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            var fragment:Fragment?=null
            if (position == 0) {
                fragment= LiveStatus()
            } else if (position == 1) {
                fragment= LivetrackingFragment()
            } else if (position == 2) {
                fragment= HistoryTrackingFragment()
            }
            return fragment!!
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            var title: String? = null
            if (position == 0) {
                title = "Live Status"
            } else if (position == 1) {
                title = "Live Tracking"
            } else if (position == 2) {
                title = "History Tracking"
            }
            return title
        }
    }

    companion object {
        private var recyclerView: RecyclerView? = null
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        var x_axis = ArrayList<String>()
        var y_axis = ArrayList<String>()
        fun newInstance(param1: String?, param2: String?): DashboardVehicleList {
            val fragment = DashboardVehicleList()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}*/
