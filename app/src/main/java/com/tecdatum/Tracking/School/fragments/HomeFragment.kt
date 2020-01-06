package com.tecdatum.Tracking.School.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tecdatum.Tracking.School.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.navigation_dashboard -> {
                    Navigation.findNavController(view!!).navigate(R.id.defaultFragment)
                    true
                }
                R.id.navigation_qv_new -> {
                    Navigation.findNavController(view!!).navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_report_new -> {
                    Navigation.findNavController(view!!).navigate(R.id.navigation_report)
                    true
                }
                R.id.navigation_administartion_new -> {
                    Navigation.findNavController(view!!).navigate(R.id.navigation_administartion)
                    true
                }
                else -> false
            }
        }
    }

}
