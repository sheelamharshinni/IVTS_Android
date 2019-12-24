package com.tecdatum.Tracking.School.models.events

import com.google.android.gms.maps.model.LatLng

class BeginJourneyEvent {
    private val event = "BEGIN_JOURNEY"
    var beginLatLng: LatLng? = null

}