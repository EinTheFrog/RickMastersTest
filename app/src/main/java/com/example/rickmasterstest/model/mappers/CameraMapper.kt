package com.example.rickmasterstest.model.mappers

import com.example.rickmasterstest.model.cache.CameraCache
import com.example.rickmasterstest.model.domain.CameraDomain
import com.example.rickmasterstest.model.domain.RoomDomain
import com.example.rickmasterstest.model.network.CameraDataNetwork

class CameraMapper {
    fun networkToDomain(cameraDataNetwork: CameraDataNetwork): List<RoomDomain> {
        val roomNameList = cameraDataNetwork.rooms
        val cameraNetworkList = cameraDataNetwork.cameras
        val roomDomainList = roomNameList.map { roomName ->
            val roomCameraNetworkList = cameraNetworkList.filter { it.room == roomName }
            val roomCameraDomainList = roomCameraNetworkList.map {
                CameraDomain(
                    name = it.name,
                    snapshot = it.snapshot,
                    id = it.id,
                    favorites = it.favorites,
                    rec = it.rec
                )
            }
            RoomDomain(
                name = roomName,
                cameras = roomCameraDomainList
            )
        }
        return roomDomainList
    }

    fun domainToCache(rooms: List<RoomDomain>): List<CameraCache> {
        val cameraCacheList = mutableListOf<CameraCache>()
        rooms.forEach { room ->
            room.cameras.forEach { camera ->
                val cameraCache = CameraCache()
                cameraCache.id = camera.id
                cameraCache.name = camera.name
                cameraCache.snapshot = camera.snapshot
                cameraCache.room = room.name
                cameraCache.favorites = camera.favorites
                cameraCache.rec = camera.rec
                cameraCacheList.add(cameraCache)
            }
        }
        return cameraCacheList
    }

    fun cacheToDomain(cameras: List<CameraCache>): List<RoomDomain> {
        val roomNameSet = mutableSetOf<String>()
        cameras.forEach { camera ->
            roomNameSet.add(camera.room)
        }

        val roomDomainList = mutableListOf<RoomDomain>()
        roomNameSet.forEach { roomName ->
            val roomCameraNetworkList = cameras.filter { it.room == roomName }
            val roomCameraDomainList = roomCameraNetworkList.map {
                CameraDomain(
                    name = it.name,
                    snapshot = it.snapshot,
                    id = it.id,
                    favorites = it.favorites,
                    rec = it.rec
                )
            }
            val roomDomain = RoomDomain(
                name = roomName,
                cameras = roomCameraDomainList
            )
            roomDomainList.add(roomDomain)
        }
        return roomDomainList
    }
}