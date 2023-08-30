package com.example.rickmasterstest.model.network

data class DoorNetwork(
    val name: String,
    val snapshot: String?,
    val room: String?,
    val id: Int,
    val favorites: Boolean
)