package com.tecdatum.Tracking.School.newhelpers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.util.*


class TimerTaskClass {


    var timer = Timer()
    fun startTimer(context: Context?) {
        Log.d("Constants", "Timer Started")
        timer.scheduleAtFixedRate(object : TimerTask() {
            @SuppressLint("DefaultLocale")
            override fun run() { //Performing my Operations
            }
        }, 0, TIME_INTERVAL.toLong())
    }

    fun stopTimer() {
        timer.cancel()
    }

    companion object {
        const val TIME_INTERVAL = 1500

    }
}






