package com.example.rickmasterstest.ui.screens.doors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmasterstest.domain.HouseRepository
import com.example.rickmasterstest.ui.screens.cameras.CamerasState
import dagger.hilt.android.lifecycle.HiltViewModel
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
            _state.value = DoorsState.Loading
            val doorsResult = houseRepository.getDoors()
            if (doorsResult.isSuccess) {
                val doors = doorsResult.getOrThrow()
                _state.value = DoorsState.Default(doors)
            } else {
                val error = doorsResult.exceptionOrNull()
                _state.value = DoorsState.Error(error)
            }
        }
    }
}