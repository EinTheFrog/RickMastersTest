package com.example.rickmasterstest.ui.screens.cameras

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmasterstest.domain.HouseRepository
import com.example.rickmasterstest.model.domain.CameraDomain
import com.example.rickmasterstest.utils.substituteElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CamerasViewModel @Inject constructor(
    private val houseRepository: HouseRepository
): ViewModel() {
    private val _state: MutableLiveData<CamerasState> = MutableLiveData(CamerasState.Loading)
    val state: LiveData<CamerasState> = _state

    fun fetchCameras() {
        viewModelScope.launch {
            fetchCameras(this)
        }
    }

    fun getLocalCameras() {
        viewModelScope.launch {
            getLocalCameras(this)
        }
    }

    fun updateCameraFavorites(selectedCamera: CameraDomain, favorites: Boolean) {
        viewModelScope.launch {
            val state = _state.value
            if (state !is CamerasState.Default) return@launch
            val oldRooms = state.roomList
            for (room in oldRooms) {
                if (room.cameras.contains(selectedCamera)) {
                    val newCamera = selectedCamera.copy(favorites = favorites)
                    val newCameras = room.cameras.substituteElement(
                        oldElement = selectedCamera,
                        newElement = newCamera
                    )
                    val newRoom = room.copy(cameras = newCameras)
                    val newRooms = oldRooms.substituteElement(
                        oldElement = room,
                        newElement = newRoom
                    )
                    houseRepository.saveRooms(newRooms)
                    _state.value = CamerasState.Default(newRooms)
                    break
                }
            }
        }
    }

    private suspend fun fetchCameras(scope: CoroutineScope) {
        _state.value = CamerasState.Loading
        val roomsResult = houseRepository.getRooms()
        if (roomsResult.isSuccess) {
            val rooms = roomsResult.getOrThrow()
            houseRepository.saveRooms(rooms)
            _state.value = CamerasState.Default(rooms)
        } else {
            val error = roomsResult.exceptionOrNull()
            _state.value = CamerasState.Error(error)
        }
    }

    private suspend fun getLocalCameras(scope: CoroutineScope) {
        _state.value = CamerasState.Loading
        val roomsResult = houseRepository.getLocalRooms()
        if (roomsResult.isSuccess) {
            val rooms = roomsResult.getOrThrow()
            if (rooms.isEmpty()) {
                fetchCameras()
            } else {
                _state.value = CamerasState.Default(rooms)
            }
        } else {
            val error = roomsResult.exceptionOrNull()
            _state.value = CamerasState.Error(error)
        }
    }
}