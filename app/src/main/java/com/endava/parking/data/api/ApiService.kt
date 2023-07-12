package com.endava.parking.data.api

import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/Account/registration")
    suspend fun signUpUser(@Body user: User): Response<String>

    @POST("api/Account/login")
    suspend fun signInUser(@Body user: Map<String, String>): Response<String>

    @FormUrlEncoded
    @POST("api/Account/changepassword")
    suspend fun restorePassword(@Field("email") email: String): Response<String>

    @GET("api/create_parking_lots")
    suspend fun createParkingLot(parkingLot: ParkingLot): Result<String>

    @GET("api/update_parking_lots")
    suspend fun updateParkingLot(parkingLot: ParkingLot): Result<String>

    @GET("api/delete_parking_lots")
    suspend fun deleteParkingLot(id: String): Result<String>

    @GET("api/parkinglots/getall")
    suspend fun fetchParkingLots(@Header("Authorization") token: String): Response<List<ParkingLot>>
}
