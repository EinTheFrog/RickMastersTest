package com.example.rickmasterstest.ui.screens.cameras

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmasterstest.domain.HouseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
            _state.value = CamerasState.Loading
            val roomsResult = houseRepository.getRooms()
            if (roomsResult.isSuccess) {
                val rooms = roomsResult.getOrThrow()
                _state.value = CamerasState.Default(rooms)
            } else {
                val error = roomsResult.exceptionOrNull()
                _state.value = CamerasState.Error(error)
            }
        }
    }
}