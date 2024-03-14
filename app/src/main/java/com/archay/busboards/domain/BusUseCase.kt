package com.archay.busboards.domain

import com.archay.busboards.data.remote.response.AdsResponse
import com.archay.busboards.data.remote.response.DeviceByTokenResponse
import com.archay.busboards.data.remote.response.TokenResponse
import com.archay.busboards.data.repository.BusRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface BusUseCase {
    fun getToken(): Flow<Result<TokenResponse>>
    fun getDeviceByToken(deviceToken: String): Flow<Result<DeviceByTokenResponse>>
    fun ads(deviceId: String): Flow<Result<List<AdsResponse>>>
}

class BusUseCaseImpl @Inject constructor(private var repository: BusRepository) : BusUseCase {
    override fun getToken(): Flow<Result<TokenResponse>> {
        return repository.getToken()
    }

    override fun getDeviceByToken(deviceToken: String): Flow<Result<DeviceByTokenResponse>> {
        return repository.getDeviceByToken(deviceToken)
    }

    override fun ads(deviceId: String): Flow<Result<List<AdsResponse>>> {
        return repository.ads(deviceId)
    }
}