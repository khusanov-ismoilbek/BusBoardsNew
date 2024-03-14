package com.archay.busboards.data.repository

import com.archay.busboards.data.remote.response.AdsResponse
import com.archay.busboards.data.remote.response.DeviceByTokenResponse
import com.archay.busboards.data.remote.response.TokenResponse
import com.archay.busboards.data.remote.service.BusService
import com.archay.busboards.local.BusPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface BusRepository {
    fun getToken(): Flow<Result<TokenResponse>>
    fun getDeviceByToken(deviceToken: String): Flow<Result<DeviceByTokenResponse>>
    fun ads(deviceId: String): Flow<Result<List<AdsResponse>>>

}

class BusRepositoryImpl @Inject constructor(
    private val service: BusService,
    private val preferences: BusPreferences
) : BusRepository {

    override fun getToken(): Flow<Result<TokenResponse>> = flow {
        val response = service.getToken()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(Result.success(it))
            }
        } else {
            emit(Result.failure(Throwable("")))
            return@flow
        }
    }.catch {
        emit(Result.failure(it))
    }.flowOn(Dispatchers.IO)

    override fun getDeviceByToken(deviceToken: String): Flow<Result<DeviceByTokenResponse>> = flow {
        val response = service.deviceByToken(deviceToken)

        if (response.isSuccessful)
            response.body()?.let {
                emit(Result.success(it))
            }
        else {
            emit(Result.failure(Throwable("")))
            return@flow
        }
    }.catch {
        emit(Result.failure(it))
    }.flowOn(Dispatchers.IO)

    override fun ads(deviceId: String): Flow<Result<List<AdsResponse>>> = flow {
        val listType = object : TypeToken<List<AdsResponse>>() {}.type

        val response = service.ads(deviceId)
        if (response.isSuccessful)
            response.body()?.let {
                preferences.slides = Gson().toJson(it)
                emit(Result.success(it))
            }
        else if (preferences.slides.isNotEmpty()) {
            emit(Result.success(Gson().fromJson(preferences.slides, listType)))
            return@flow
        } else {
            emit(Result.failure(Throwable("")))
            return@flow
        }

    }.catch {
        val listType = object : TypeToken<List<AdsResponse>>() {}.type
        if (preferences.slides.isNotEmpty())
            emit(Result.success(Gson().fromJson(preferences.slides, listType)))
        else
            emit(Result.failure(it))
    }.flowOn(Dispatchers.IO)

    private fun parseJsonToList(jsonString: String): List<AdsResponse> {
        val listType = object : TypeToken<List<AdsResponse>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

}