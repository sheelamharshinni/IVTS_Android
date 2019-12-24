package com.tecdatum.Tracking.School.models.events

import com.google.android.gms.maps.model.LatLng

class CurrentJourneyEvent {
    private val event = "BEGIN_JOURNEY"
    var currentLatLng: LatLng? = null

}