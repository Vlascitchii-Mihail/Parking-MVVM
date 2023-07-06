package com.endava.parking.data.api

import com.endava.parking.data.model.ParkingLotToRequest
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.GET

interface ApiService {

    @POST("api/Account/registration")
    suspend fun signUpUser(@Body user: User): Response<String>

    @POST("api/Account/login")
    suspend fun signInUser(@Body user: Map<String, String>): Response<String>

    @POST("api/Account/changepassword")
    suspend fun restorePassword(@Query("email") email: String): Response<String>

    @POST("api/parkinglots")
    suspend fun createParkingLot(@Header("Authorization") token: String?, @Body parkingLot: ParkingLotToRequest): Response<String>

    //TODO add function to get data for User Details Parking Lot
    @GET("")
    suspend fun getParkingLotDescription(@Header("Authorization") token: String?, parkingId: String): Response<ParkingLot>

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
}
