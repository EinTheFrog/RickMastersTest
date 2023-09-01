package com.example.rickmasterstest.domain

import com.example.rickmasterstest.model.domain.DoorDomain
import com.example.rickmasterstest.model.domain.RoomDomain

interface HouseRepository {
    suspend fun getRooms(): Result<List<RoomDomain>>

    suspend fun getLocalRooms(): Result<List<RoomDomain>>

    suspend fun getDoors(): Result<List<DoorDomain>>

    suspend fun getLocalDoors(): Result<List<DoorDomain>>

    suspend fun saveRooms(rooms: List<RoomDomain>): Result<Unit>

    suspend fun saveDoors(doors: List<DoorDomain>): Result<Unit>
}