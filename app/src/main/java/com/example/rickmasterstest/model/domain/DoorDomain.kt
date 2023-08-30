package com.example.rickmasterstest.model.domain

data class DoorDomain(
    val name: String,
    val snapshot: String?,
    val id: Int,
    val favorites: Boolean
)