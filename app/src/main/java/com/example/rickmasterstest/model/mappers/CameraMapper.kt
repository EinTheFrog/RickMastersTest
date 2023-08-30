package com.example.rickmasterstest.model.mappers

import com.example.rickmasterstest.model.domain.CameraDomain
import com.example.rickmasterstest.model.domain.RoomDomain
import com.example.rickmasterstest.model.network.DataNetwork

class CameraMapper {
    fun networkToDomain(dataNetwork: DataNetwork): List<RoomDomain> {
        val roomNameList = dataNetwork.rooms
        val cameraNetworkList = dataNetwork.cameras
        val roomDomainList = roomNameList.map { roomName ->
            val roomCameraNetworkList = cameraNetworkList.filter { it.room == roomName }
            val roomCameraDomainList = roomCameraNetworkList.map {
                CameraDomain(
                    name = it.name,
                    snapshot = it.snapshot,
                    id = it.id,
                    favorites = it.favorites,
                    rect = it.rect
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