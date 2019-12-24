package com.tecdatum.Tracking.School.models.events

import com.google.android.gms.maps.model.LatLng

class EndJourneyEvent {
    var event = "END_JOURNEY"
    var endJourneyLatLng: LatLng? = null

}