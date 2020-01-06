package com.tecdatum.Tracking.School.newactivities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.tecdatum.Tracking.School.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class HomeActivity_new : AppCompatActivity(), View.OnClickListener {
    var navController: NavController? = null

    companion object {
        var SBTSApplication: HomeActivity_new =
                HomeActivity_new()

        fun getInstance(): HomeActivity_new {
            if (SBTSApplication == null) {
                SBTSApplication =
                        HomeActivity_new()
            }
            return SBTSApplication
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        SBTSApplication = this
        setupNavigation()
        sidemenu()
        lay_back.visibility = View.GONE
    }

    private fun setupNavigation() {

        setSupportActionBar(toolbar)
        if (toolbar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController!!, drawer_layout)
        NavigationUI.setupWithNavController(navigationView, navController!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
                drawer_layout,
                Navigation.findNavController(this, R.id.nav_host_fragment)
        )
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun sidemenu() {
        lay_contact.setOnClickListener(this)
        lay_logout.setOnClickListener(this)


    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.lay_contact -> {
                navController!!.navigate(R.id.contactusFragment)
                drawer_layout.closeDrawer(GravityCompat.START)

            }
            R.id.lay_logout -> {
                logoutdialog()
                drawer_layout.closeDrawer(GravityCompat.START)

            }

        }
    }

    fun logoutdialog() {
        val dialogs = Dialog(this@HomeActivity_new)
        dialogs.setContentView(R.layout.dialog_logout)
        dialogs.window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialogs.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val yesBtn = dialogs.findViewById(R.id.lay_logout_s) as RelativeLayout
        val noBtn = dialogs.findViewById(R.id.lay_cancel) as RelativeLayout
        yesBtn.setOnClickListener {
            val intent = Intent(this@HomeActivity_new, LoginActivity_New::class.java)
            startActivity(intent)
            dialogs.dismiss()
        }
        noBtn.setOnClickListener { dialogs.dismiss() }
        dialogs.show()
    }

}