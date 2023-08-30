package com.example.rickmasterstest.ui.screens.cameras

import com.example.rickmasterstest.model.domain.RoomDomain
import java.lang.Exception

sealed interface CamerasState {
    data class Default(
        val roomList: List<RoomDomain>
    ): CamerasState

    object Loading: CamerasState

    data class Error(
        val exception: Throwable?
    ): CamerasState
}