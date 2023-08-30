package com.example.rickmasterstest.model.network

import com.google.gson.annotations.SerializedName

data class DataNetwork(
    @SerializedName("room") val rooms: List<String>,
    val cameras: List<CameraNetwork>
)