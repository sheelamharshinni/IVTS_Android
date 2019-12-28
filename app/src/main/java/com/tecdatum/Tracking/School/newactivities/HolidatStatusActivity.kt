package com.tecdatum.Tracking.School.newactivities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.tecdatum.Tracking.School.R
import com.tecdatum.Tracking.School.fragments.ExceptionReportFragment
import kotlinx.android.synthetic.main.activity_holidat_status.*
import kotlinx.android.synthetic.main.activity_holiday.navigation

class HolidatStatusActivity : AppCompatActivity() {
    var Reason: String? = null
    var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidat_status)
        val prefs = getSharedPreferences(SplashScreen.MY_PREFS_NAME, Context.MODE_PRIVATE)
        message = prefs.getString("Message", "")
        Reason = prefs.getString("Reason", "")
        Holiday_Status.setText(Reason)

        navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.navigation_Profile -> {
                    val intent = Intent(this@HolidatStatusActivity, Edit_ParentDetails::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_Logout -> {
                    logoutdialog()

                    true


                }
                R.id.navigation_history -> {
                    val intent = Intent(this@HolidatStatusActivity, PArentHistoryTrackingDetails::class.java)
                    startActivity(intent)
                    true


                }
                else -> false
            }
        }
    }

    fun logoutdialog() {
        val dialogs = Dialog(this@HolidatStatusActivity)
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
