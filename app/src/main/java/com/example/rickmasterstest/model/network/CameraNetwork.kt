package com.example.rickmasterstest.model.network

data class CameraNetwork(
    val name: String,
    val snapshot: String,
    val room: String,
    val id: Int,
    val favorites: Boolean,
    val rec: Boolean
)