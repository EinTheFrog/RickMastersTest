package com.example.rickmasterstest.domain

import com.example.rickmasterstest.data.network.HouseApi
import com.example.rickmasterstest.model.domain.RoomDomain
import com.example.rickmasterstest.model.mappers.CameraMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HouseRepositoryImpl @Inject constructor(
    private val houseApi: HouseApi,
    private val cameraMapper: CameraMapper
): HouseRepository {
    override suspend fun getRooms(): Result<List<RoomDomain>> = withContext(Dispatchers.IO) {
        val call = houseApi.getCamerasData()
        val response = call.execute()
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            val dataNetwork = responseBody.data
            return@withContext Result.success(cameraMapper.networkToDomain(dataNetwork))
        } else {
            return@withContext Result.failure(Exception(response.errorBody()?.toString()))
        }
    }

    override suspend fun getDoors() = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }
}