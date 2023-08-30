package com.example.rickmasterstest.model.domain

data class RoomDomain(
    val name: String,
    val cameras: List<CameraDomain>
)