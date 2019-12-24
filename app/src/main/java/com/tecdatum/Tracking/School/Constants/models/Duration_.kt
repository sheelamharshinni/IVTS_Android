package com.tecdatum.Tracking.School.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Duration_ {
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("value")
    @Expose
    var value: Int? = null

}