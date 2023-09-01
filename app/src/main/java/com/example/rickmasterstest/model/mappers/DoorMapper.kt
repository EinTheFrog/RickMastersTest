package com.example.rickmasterstest.model.mappers

import com.example.rickmasterstest.model.cache.DoorCache
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

    fun domainToCache(doors: List<DoorDomain>): List<DoorCache> {
        return doors.map {
            val doorCache = DoorCache()
            doorCache.id = it.id
            doorCache.name = it.name
            doorCache.snapshot = it.snapshot
            doorCache.favorites = it.favorites
            doorCache
        }
    }

    fun cacheToDomain(doors: List<DoorCache>): List<DoorDomain> {
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