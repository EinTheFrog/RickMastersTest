package com.example.rickmasterstest.model.network

import com.google.gson.annotations.SerializedName

data class DoorResponseNetwork(
    val success: Boolean,
    @SerializedName("data") val doors: List<DoorNetwork>
)