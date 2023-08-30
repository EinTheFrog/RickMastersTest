package com.example.rickmasterstest.model.mappers

import com.example.rickmasterstest.model.domain.DoorDomain
import com.example.rickmasterstest.model.network.DoorNetwork

class DoorMapper {
    fun networkToDomain(doors: List<DoorNetwork>): List<DoorDomain> {
        return doors.map {
            DoorDomain(
                name = it.name,
                snapshot = it.snapshot,
                id = it.id,
                favorites = it.favorites
            )
        }
    }
}