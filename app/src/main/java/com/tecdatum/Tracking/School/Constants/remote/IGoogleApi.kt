package com.tecdatum.Tracking.School.Constants.remote


import com.tecdatum.Tracking.School.models.Result
import com.tecdatum.Tracking.School.volley.POJO.Example
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IGoogleApi {

    @POST("IVTSSchool/LiveTracking")
    fun Places(@Body requestBody: RequestBody?): Call<String?>?

    @POST("IVTSSchool/PointsMaster")
    fun points(@Body requestBody: RequestBody?): Call<String?>?

    @GET("maps/api/directions/json")
    fun getDirections(@Query("mode") mode: String,
                      @Query("transit_routing_preference") routingPreference: String,
                      @Query("origin") origin: String,
                      @Query("destination") destination: String,
                      @Query("key") apiKey: String): Single<Result>




}