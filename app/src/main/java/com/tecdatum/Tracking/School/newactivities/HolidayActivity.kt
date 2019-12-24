package com.tecdatum.Tracking.School.newactivities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.tecdatum.Tracking.School.R
import com.tecdatum.Tracking.School.fragments.ExceptionReportFragment
import kotlinx.android.synthetic.main.activity_holiday.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class HolidayActivity : AppCompatActivity() {
    var Message: String? = null
    var Reason: String? = null
    var UserName: String? = null
    var VehicleNumber: String? = null
    var RouteName: String? = null
    var RouteID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)
        lay_back.visibility = View.INVISIBLE
        lay_back.isEnabled = false
        val bb: SharedPreferences = getSharedPreferences(SplashScreen.MY_PREFS_NAME, Context.MODE_PRIVATE)
        UserName = bb.getString("ParentName", "")
        VehicleNumber = bb.getString("VehicleNumber", "")
        RouteName = bb.getString("RouteName", "")
        RouteID = bb.getString("RouteID", "")

        if (UserName != null) {
            if (UserName != "") {
                textView.text = "Welcome  $UserName"
            } else {
                textView.visibility = View.GONE
            }
        } else {
            textView.visibility = View.GONE
        }
        Message = bb.getString("Message", "")
        Reason = bb.getString("Reason", "")
        tv_reason.setText(Reason)
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
                else -> false
            }
        }
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
}
