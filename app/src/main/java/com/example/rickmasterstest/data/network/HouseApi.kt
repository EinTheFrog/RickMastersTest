package com.example.rickmasterstest.data.network

import com.example.rickmasterstest.model.network.CameraResponseNetwork
import com.example.rickmasterstest.model.network.DoorResponseNetwork
import retrofit2.Call
import retrofit2.http.GET

interface HouseApi {
    @GET("cameras")
    fun getCameras(): Call<CameraResponseNetwork>

    @GET("doors")
    fun getDoors(): Call<DoorResponseNetwork>
}