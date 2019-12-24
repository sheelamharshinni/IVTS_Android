package com.tecdatum.Tracking.School.newactivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.tecdatum.Tracking.School.R
import com.tecdatum.Tracking.School.newConstants.Url_new
import com.tecdatum.Tracking.School.newhelpers.Samplemyclass
import com.tecdatum.Tracking.School.volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_edit__parent_details.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

class Edit_ParentDetails : AppCompatActivity() {
    var ParentMobile: String? = null
    var UserName: String? = null
    var DisplayName: String? = null
    var StudentID: String? = null
    var StudentClass: String? = null
    var ParentName: String? = null
    var S_et_oldnumber: String? = null
    var S_et_newnumber: String? = null
    var S_et_Displayname: String? = null
    var StudentClass_arraylist = ArrayList<Samplemyclass>()
    var sp_class_id: String? = null
    var sp_class_Name: String? = null
    var Master = Url_new.Master
    var UpdateRegistrationData = Url_new.UpdateRegistrationData

    var S_et_confirmmobilenumber: kotlin.String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit__parent_details)
        StudentClass()
        val bb: SharedPreferences = getSharedPreferences(SplashScreen.MY_PREFS_NAME, Context.MODE_PRIVATE)
        ParentMobile = bb.getString("ParentMobile", "")
        UserName = bb.getString("UserName", "")
        StudentID = bb.getString("StudentID", "")
        DisplayName = bb.getString("DisplayName", "")
        ParentName = bb.getString("ParentName", "")
        StudentClass = bb.getString("StudentClass", "")

        if (ParentMobile != null) {
            et_oldnumber!!.setText(ParentMobile)
        } else {
            et_oldnumber!!.setText("")
        }
        if (UserName != null) {
            et_studentname.setText(UserName)
        } else {
            et_studentname.setText("")
        }
        if (ParentName != null) {
            et_ParentName.setText(ParentName)
        } else {
            et_ParentName.setText("")
        }
        if (DisplayName != null) {
            et_Displayname!!.setText(DisplayName)
        } else {
            et_Displayname!!.setText("")
        }
        lay_back.setOnClickListener {
            finish()
        }
        bt_Cancel.setOnClickListener{
            finish()
        }
        bt_updateinfo!!.setOnClickListener {
            StringNotNul()

            if (sp_class != null && sp_class.selectedItem != null) {
                if (!(sp_class.selectedItem.toString().trim { it <= ' ' } === "Select")) {
                    if (!S_et_Displayname.equals("", ignoreCase = true)) {
                        if (!S_et_oldnumber.equals("", ignoreCase = true)) {
                            if (!S_et_newnumber.equals("", ignoreCase = true)) {
                                if (S_et_newnumber!!.length == 10) {
                                    if (!S_et_confirmmobilenumber.equals("", ignoreCase = true)) {
                                        if (S_et_confirmmobilenumber!!.length == 10) {
                                            if (S_et_newnumber == S_et_confirmmobilenumber) {
                                                update(S_et_confirmmobilenumber!!)
                                            } else {
                                                Toast.makeText(applicationContext, "New Mobile Number and Confirm Mobile Number Should be equal", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            Toast.makeText(applicationContext, "Confirm Mobile Number Should be 10 digits", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(applicationContext, "Please Enter Confirm  Number", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(applicationContext, "Mobile Number Should be 10 digits", Toast.LENGTH_SHORT).show()
                                }


                            } else {
                                update(S_et_oldnumber!!)
                            }
                        } else {
                            Toast.makeText(this@Edit_ParentDetails, "Old Number Must", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@Edit_ParentDetails, "Please Enter Display Name", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Edit_ParentDetails, "Please Select Class", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@Edit_ParentDetails, "Please Select Class", Toast.LENGTH_SHORT).show()
            }


        }

    }

    private fun update(NewMobilenumber: String) {
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()
        try {
            jsonObject.put("OldMobileNumber", S_et_oldnumber)
            jsonObject.put("NewMobileNumber", NewMobilenumber)
            jsonObject.put("StudentID", StudentID)
            jsonObject.put("DisplayName", S_et_Displayname)
            jsonObject.put("ClassID", sp_class_id)
            jsonObject.put("ParentName", et_ParentName.text.toString())

            Log.d("Request", "VOLLEYATRINGREQUEST$jsonObject")
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, UpdateRegistrationData, jsonObject, Response.Listener { response ->
                Log.d("Response", response.toString())
                val code = response.optString("Code", "0")
                val message = response.optString("Message", "Success")
                if (code.equals("1", ignoreCase = true)) {
                    Toast.makeText(this@Edit_ParentDetails, message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Edit_ParentDetails, LoginActivity_New::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Edit_ParentDetails, message, Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { }
            )
            requestQueue.add(jsonObjectRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun StringNotNul() {
        S_et_oldnumber = et_oldnumber.text.toString()
        S_et_newnumber = et_newnumber.text.toString()
        S_et_Displayname = et_Displayname.text.toString()
        S_et_confirmmobilenumber = et_confirmmobilenumber.text.toString()
        if (S_et_oldnumber == null) {
            S_et_oldnumber = ""
        } else {
        }
        if (S_et_newnumber == null) {
            S_et_newnumber = ""
        } else {
        }
        if (S_et_Displayname == null) {
            S_et_Displayname = ""
        } else {
        }

        if (S_et_confirmmobilenumber != null) {
        } else {
            S_et_confirmmobilenumber = ""
        }
    }

    private fun StudentClass_Master(str1: ArrayList<Samplemyclass>) {
        val adapter = ArrayAdapter(this,
                R.layout.spinner_item, str1)
        sp_class.adapter = adapter
        for (i in StudentClass_arraylist.indices) {
            if (StudentClass_arraylist[i].name == StudentClass) {
                sp_class.setSelection(i)
            } else {
                Log.d("SELECTEDMONTH", "FALSE")
            }
        }
        sp_class.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if (view != null) {
                    (adapterView.getChildAt(0) as TextView).setTextColor(Color.parseColor("#000000"))
                }
                val pos = adapterView.selectedItemPosition
                if (pos != 0) {
                    (adapterView.getChildAt(0) as TextView).setTextColor(Color.parseColor("#000000"))
                    val country = adapterView.selectedItem as Samplemyclass
                    sp_class_id = country.id
                    sp_class_Name = country.name
                    Log.e("sp_class_id_name", "sp_class_id_name$sp_class_id")
                } else {
                    sp_class_id = ""
                    sp_class_Name = ""
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun monthdayreport(ID: String) {
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()
        try {
            jsonObject.put("ActionName", "GetStudentDetailsByID")
            jsonObject.put("StudentMasterID", ID)
            Log.d("Request", jsonObject.toString())
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, ParentRegistrationActivity.StudentDetailsForLogin, jsonObject, Response.Listener { response ->
                Log.d("Response", response.toString())
                try {
                    val `object` = JSONObject(response.toString())
                    val code = response.optString("Code", "0")
                    val message = response.optString("Message", "Success")
                    if (code.equals("0", ignoreCase = true)) {
                        val jsonArray = `object`.getJSONArray("StudentData")
                        val number = jsonArray.length()
                        val num = Integer.toString(number)
                        if (number == 0) {
                        } else {
                            for (i in 0 until number) {
                                try {
                                    val jsonObject = jsonArray.getJSONObject(i)
                                    Log.d("Request", jsonObject.toString())
                                    val StudentMasterID = jsonObject.getString("StudentMasterID")
                                    val StudentName = jsonObject.getString("StudentName")
                                    val StudentID = jsonObject.getString("StudentID")
                                    val ClassName = jsonObject.getString("ClassName")
                                    val ParentName = jsonObject.getString("ParentName")
                                    val ParentMobile = jsonObject.getString("ParentMobile")
                                    val ParentMobile2 = jsonObject.getString("ParentMobile2")
                                    val RouteID = jsonObject.getString("RouteID")
                                    val PickPointID = jsonObject.getString("PickPointID")
                                    val DropPointID = jsonObject.getString("DropPointID")
                                    if (ParentName != null) {
                                        et_ParentName.setText(ParentName)
                                    } else {
                                    }
                                    if (ParentMobile != null) {
                                        et_oldnumber.setText(ParentMobile)
                                    } else {
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    } else {
                        val alertDialog = AlertDialog.Builder(this)
                        alertDialog.setTitle("Response")
                        alertDialog.setMessage("" + message)
                        alertDialog.setPositiveButton("Ok") { dialog, which ->
                            // Write your code here to invoke YES event
                            dialog.cancel()
                        }
                        alertDialog.show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { }
            )
            requestQueue.add(jsonObjectRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun StudentClass() {
        val jsonBody = JSONObject()
        try {
            jsonBody.put("Action", "" + "StudentClass")
            val mRequestBody = jsonBody.toString()
            Log.e("VOLLEY", "GetQuarterdropdown$mRequestBody")
            val stringRequest: StringRequest = object : StringRequest(Method.POST, Master, Response.Listener { response ->
                Log.e("VOLLEY", "GetQuarterdropdown$response")
                try {
                    val `object` = JSONObject(response)
                    val code = `object`.optString("Code")
                    val message = `object`.optString("Message")
                    StudentClass_arraylist.clear()
                    if (code.equals("0", ignoreCase = true)) {
                        StudentClass_arraylist.clear()
                        val jArray = `object`.getJSONArray("Data")
                        val number = jArray.length()
                        val num = Integer.toString(number)
                        if (number == 0) {
                            Toast.makeText(this@Edit_ParentDetails, " No Data Found", Toast.LENGTH_LONG).show()
                        } else {
                            val wp0 = Samplemyclass("0", "Select")
                            StudentClass_arraylist.add(wp0)
                            for (i in 0 until number) {
                                val json_data = jArray.getJSONObject(i)
                                val e_id = json_data.getString("Id")
                                val e_n = json_data.getString("Data")
                                val wp = Samplemyclass(e_id, e_n)
                                StudentClass_arraylist.add(wp)
                            }
                        }
                        if (StudentClass_arraylist.size > 0) {
                            StudentClass_Master(StudentClass_arraylist)
                        }
                    } else {
                        val wp0 = Samplemyclass("0", "Select")
                        // Binds all strings into an array
                        StudentClass_arraylist.add(wp0)
                        if (StudentClass_arraylist.size > 0) {
                            StudentClass_Master(StudentClass_arraylist)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> Log.e("VOLLEY", error.toString()) }) {
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
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


}
