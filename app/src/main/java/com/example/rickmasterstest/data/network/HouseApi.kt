package com.example.rickmasterstest.data.network

import com.example.rickmasterstest.model.network.DataNetwork
import com.example.rickmasterstest.model.network.ResponseNetwork
import retrofit2.Call
import retrofit2.http.GET

interface HouseApi {
    @GET("cameras")
    fun getCamerasData(): Call<ResponseNetwork>
}