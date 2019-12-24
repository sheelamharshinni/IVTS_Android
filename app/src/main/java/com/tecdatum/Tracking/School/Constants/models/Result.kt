package com.tecdatum.Tracking.School.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Amanjeet Singh on 8/9/18.
 */
class Result {
    @SerializedName("geocoded_waypoints")
    @Expose
    var geocodedWaypoints: List<GeocodedWaypoint>? = null
    @SerializedName("routes")
    @Expose
    var routes: List<Route>? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}