package com.endava.parking.data.api

import com.endava.parking.data.model.ParkingLotToRequest
import com.endava.parking.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/Account/registration")
    suspend fun signUpUser(@Body user: User): Response<String>

    @POST("api/Account/login")
    suspend fun signInUser(@Body user: Map<String, String>): Response<String>

    @POST("api/Account/changepassword")
    suspend fun restorePassword(@Query("email") email: String): Response<String>

    @POST("api/parkinglots")
    suspend fun createParkingLot(@Header("Authorization") token: String?, @Body parkingLot: ParkingLotToRequest): Response<String>
}
