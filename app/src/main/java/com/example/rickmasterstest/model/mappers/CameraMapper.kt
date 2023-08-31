package com.example.rickmasterstest.model.mappers

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
}