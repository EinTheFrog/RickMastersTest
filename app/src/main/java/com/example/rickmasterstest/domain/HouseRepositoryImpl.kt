package com.example.rickmasterstest.domain

import com.example.rickmasterstest.data.network.HouseApi
import com.example.rickmasterstest.model.cache.CameraCache
import com.example.rickmasterstest.model.cache.DoorCache
import com.example.rickmasterstest.model.domain.DoorDomain
import com.example.rickmasterstest.model.domain.RoomDomain
import com.example.rickmasterstest.model.mappers.CameraMapper
import com.example.rickmasterstest.model.mappers.DoorMapper
import io.realm.Realm
import io.realm.kotlin.delete
import io.realm.kotlin.executeTransactionAwait
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HouseRepositoryImpl @Inject constructor(
    private val houseApi: HouseApi,
    private val cameraMapper: CameraMapper,
    private val doorMapper: DoorMapper,
    private val realm: Realm
): HouseRepository {
    override suspend fun getRooms(): Result<List<RoomDomain>> = withContext(Dispatchers.IO) {
        val call = houseApi.getCameras()
        try {
            val response = call.execute()
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val dataNetwork = responseBody.data
                return@withContext Result.success(cameraMapper.networkToDomain(dataNetwork))
            } else {
                throw Exception(response.errorBody()?.toString())
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun getLocalRooms() = withContext(Dispatchers.Main) {
        val cameras : List<CameraCache> = realm.where<CameraCache>().findAll()
        val roomDomainList = cameraMapper.cacheToDomain(cameras)
        return@withContext Result.success(roomDomainList)
    }

    override suspend fun getDoors() = withContext(Dispatchers.IO) {
        val call = houseApi.getDoors()
        try {
            val response = call.execute()
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val doors = responseBody.doors
                return@withContext Result.success(doorMapper.networkToDomain(doors))
            } else {
                throw Exception(response.errorBody()?.toString())
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun getLocalDoors() = withContext(Dispatchers.Main) {
        val doors : List<DoorCache> = realm.where<DoorCache>().findAll()
        val doorDomainList = doorMapper.cacheToDomain(doors)
        return@withContext Result.success(doorDomainList)
    }

    override suspend fun saveRooms(rooms: List<RoomDomain>) = withContext(Dispatchers.IO) {
        val cameraCacheList = cameraMapper.domainToCache(rooms)
        try {
            realm.executeTransactionAwait { transactionRealm ->
                transactionRealm.delete<CameraCache>()
                transactionRealm.insert(cameraCacheList)
            }
            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun saveDoors(doors: List<DoorDomain>) = withContext(Dispatchers.IO) {
        val doorCacheList = doorMapper.domainToCache(doors)
        try {
            realm.executeTransactionAwait { transactionRealm ->
                transactionRealm.delete<DoorCache>()
                transactionRealm.insert(doorCacheList)
            }
            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}