package com.example.rickmasterstest.domain

import com.example.rickmasterstest.model.domain.RoomDomain

interface HouseRepository {
    suspend fun getRooms(): Result<List<RoomDomain>>
    suspend fun getDoors()
}