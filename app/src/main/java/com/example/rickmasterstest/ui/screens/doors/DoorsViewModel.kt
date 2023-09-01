package com.example.rickmasterstest.ui.screens.doors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmasterstest.domain.HouseRepository
import com.example.rickmasterstest.model.domain.DoorDomain
import com.example.rickmasterstest.utils.substituteElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoorsViewModel @Inject constructor(
    private val houseRepository: HouseRepository
): ViewModel() {
    private val _state: MutableLiveData<DoorsState> = MutableLiveData(DoorsState.Loading)
    val state: LiveData<DoorsState> = _state

    fun fetchDoors() {
        viewModelScope.launch {
            fetchDoors(this)
        }
    }

    fun getLocalDoors() {
        viewModelScope.launch {
            getLocalDoors(this)
        }
    }

    fun updateDoorFavorites(selectedDoor: DoorDomain, favorites: Boolean) {
        viewModelScope.launch {
            val state = _state.value
            if (state !is DoorsState.Default) return@launch
            val oldDoors = state.doorList
            val newDoor = selectedDoor.copy(favorites = favorites)
            val newDoors = oldDoors.substituteElement(
                oldElement = selectedDoor,
                newElement = newDoor
            )
            houseRepository.saveDoors(newDoors)
            _state.value = DoorsState.Default(newDoors)
        }
    }

    fun updateDoorName(selectedDoor: DoorDomain, name: String) {
        viewModelScope.launch {
            val state = _state.value
            if (state !is DoorsState.Default) return@launch
            val oldDoors = state.doorList
            val newDoor = selectedDoor.copy(name = name)
            val newDoors = oldDoors.substituteElement(
                oldElement = selectedDoor,
                newElement = newDoor
            )
            houseRepository.saveDoors(newDoors)
            _state.value = DoorsState.Default(newDoors)
        }
    }

    private suspend fun fetchDoors(scope: CoroutineScope) {
        _state.value = DoorsState.Loading
        val doorsResult = houseRepository.getDoors()
        if (doorsResult.isSuccess) {
            val doors = doorsResult.getOrThrow()
            houseRepository.saveDoors(doors)
            _state.value = DoorsState.Default(doors)
        } else {
            val error = doorsResult.exceptionOrNull()
            _state.value = DoorsState.Error(error)
        }
    }

    private suspend fun getLocalDoors(scope: CoroutineScope) {
        _state.value = DoorsState.Loading
        val doorsResult = houseRepository.getLocalDoors()
        if (doorsResult.isSuccess) {
            val doors = doorsResult.getOrThrow()
            if (doors.isEmpty()) {
                fetchDoors()
            } else {
                _state.value = DoorsState.Default(doors)
            }
        } else {
            val error = doorsResult.exceptionOrNull()
            _state.value = DoorsState.Error(error)
        }
    }
}