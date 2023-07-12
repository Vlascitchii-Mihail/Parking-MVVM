package com.endava.parking.data.api

import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/Account/registration")
    suspend fun signUpUser(@Body user: User): Response<String>

    @POST("api/Account/login")
    suspend fun signInUser(@Body user: Map<String, String>): Response<String>

    @FormUrlEncoded
    @POST("api/Account/changepassword")
    suspend fun restorePassword(@Query("email") email: String): Response<String>

    @GET("api/ParkingSpots/getspots")
    suspend fun getParkingSpots(
        @Header("Authorization") token: String?,
        @Query("lotName") parkingLotName: String,
        @Query("levelName") levelName: String
    ): Response<ParkingLot>

    @POST("api/ParkingSpots/createbyuser")
    suspend fun takeUpSpot(
        @Header("Authorization") token: String?,
        @Body spotDescription: Map<String, String>
    ): Response<String>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("api/parkinglots/getall")
    suspend fun fetchParkingLots(@Header("Authorization") token: String): Response<List<ParkingLot>>

    @GET("api/create_parking_lots")
    suspend fun createParkingLot(parkingLot: ParkingLot): Response<String>

    @GET("api/update_parking_lots")
    suspend fun updateParkingLot(parkingLot: ParkingLot): Response<String>

    @GET("api/delete_parking_lots")
    suspend fun deleteParkingLot(id: String): Response<String>
}
