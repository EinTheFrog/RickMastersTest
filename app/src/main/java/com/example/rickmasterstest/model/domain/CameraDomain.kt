package com.example.rickmasterstest.model.domain

data class CameraDomain(
    val name: String,
    val snapshot: String,
    val id: Int,
    val favorites: Boolean,
    val rect: Boolean
)