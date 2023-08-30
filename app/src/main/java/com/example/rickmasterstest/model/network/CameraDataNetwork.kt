package com.example.rickmasterstest.model.network

import com.google.gson.annotations.SerializedName

data class CameraDataNetwork(
    @SerializedName("room") val rooms: List<String>,
    val cameras: List<CameraNetwork>
)