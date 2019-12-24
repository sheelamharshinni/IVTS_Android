package com.tecdatum.Tracking.School.utils

import com.tecdatum.Tracking.School.newhelpers.Vehiclepoints

public class WayPointsUtils(private val wayPoints: Array<Vehiclepoints>, private val initialLoading: Int = 10) {

    private var dataLoadedCount = 0
    private var requestCount = 0
    private var callback: DataLoadCallbacks? = null

    fun setCallback(callback: DataLoadCallbacks) {
        this.callback = callback
    }

    fun loadWayPointsData() {
        if (wayPoints.size <= initialLoading) {
            // load all at once
            getData(wayPoints)
        } else {
            // load in chunks
            getData(wayPoints.copyOfRange(0, initialLoading))
        }
    }

    fun loadNext(count: Int) {
        if (requestCount + count <= wayPoints.size) {
            // get count more way points data
            getData(wayPoints.copyOfRange(requestCount, requestCount + count))
        } else {
            // get all remaining way points data
            getData(wayPoints.copyOfRange(requestCount, wayPoints.size - requestCount))
        }
    }

    fun getData(wayPoints: Array<Vehiclepoints>) {
        // make api request to get data
        requestCount += wayPoints.size

        // when data is fetched from api
        val polylines: Array<String> = arrayOf()
        dataLoadedCount += wayPoints.size
        callback?.onDataLoaded(polylines)
        if (isDataLoaded())
            callback?.onLoadFinished()
    }

    fun isDataLoaded(): Boolean {
        return dataLoadedCount == wayPoints.size
    }

    interface DataLoadCallbacks {
        fun onDataLoaded(polylines: Array<String>)
        fun onLoadFinished()
    }

}