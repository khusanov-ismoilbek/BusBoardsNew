package com.archay.busboards.data.remote.service

import com.archay.busboards.data.BusUrl
import com.archay.busboards.data.remote.response.AdsResponse
import com.archay.busboards.data.remote.response.DeviceByTokenResponse
import com.archay.busboards.data.remote.response.TokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BusService {

    @GET(BusUrl.TOKEN)
    suspend fun getToken(): Response<TokenResponse?>

    @GET(BusUrl.ADS)
    suspend fun ads(@Query("device_id") deviceId: String): Response<List<AdsResponse>>

    @GET(BusUrl.DEVICE_BY_TOKEN)
    suspend fun deviceByToken(@Query("token") token: String): Response<DeviceByTokenResponse?>
}