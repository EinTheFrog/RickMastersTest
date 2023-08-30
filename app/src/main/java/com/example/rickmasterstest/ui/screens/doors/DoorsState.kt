package com.example.rickmasterstest.ui.screens.doors

import com.example.rickmasterstest.model.domain.DoorDomain

sealed interface DoorsState {
    data class Default(
        val doorList: List<DoorDomain>
    ): DoorsState

    object Loading: DoorsState

    data class Error(
        val exception: Throwable?
    ): DoorsState
}