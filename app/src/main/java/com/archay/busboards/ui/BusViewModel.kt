package com.archay.busboards.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.archay.busboards.base.BaseViewModel
import com.archay.busboards.data.dto.AdsDto
import com.archay.busboards.data.dto.DeviceByTokenDto
import com.archay.busboards.data.dto.TokenDto
import com.archay.busboards.domain.BusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(private var useCase: BusUseCase) : BaseViewModel() {

    val token = MutableLiveData<TokenDto>()
    val deviceByToken = MutableLiveData<DeviceByTokenDto>()
    val ads = MutableLiveData<List<AdsDto>>()

    val adsLoading = MutableLiveData<Boolean>()
    val deviceByTokenFailure = MutableLiveData<Boolean>()

    fun getToken() {
        useCase.getToken().onEach {
            it.onSuccess { tokenResponse ->
                token.postValue(tokenResponse.mapToDto())
            }
            it.onFailure {

            }
        }.launchIn(viewModelScope)

    }

    fun getDeviceByToken(deviceToken: String) {
        if (deviceToken.isEmpty())
            return
        useCase.getDeviceByToken(deviceToken).onEach {
            it.onSuccess { deviceByTokenResponse ->
                deviceByToken.postValue(deviceByTokenResponse.mapToDto())
            }
            it.onFailure {
                Log.d("TTTT", "getDeviceByToken onFailure")
                deviceByTokenFailure.postValue(true)
            }
        }.launchIn(viewModelScope)
    }

    fun ads(deviceId: String) {
        if (deviceId.isEmpty())
            return
        adsLoading.value = true

        useCase.ads(deviceId).onEach {
            it.onSuccess { list ->
                Log.d("TTTT", "BusViewModel ads onSuccess: $list")
                ads.postValue(list.map { adsResponse ->
                    adsResponse.mapToDto()
                })

                adsLoading.value = false
            }
            it.onFailure {
                Log.d("TTTT", "BusViewModel ads onFailure")
                adsLoading.value = false
            }
        }.launchIn(viewModelScope)
    }

    fun previousStations(): List<StationDto> {

        val list = ArrayList<StationDto>()

        list.add(StationDto("Toshknet MUM"))
        list.add(StationDto("Foton IChB"))
        list.add(StationDto("Kurantlar bekati"))
        list.add(StationDto("6-tugruxona"))
        list.add(StationDto("Uspenskiy nomli musiqa maktabi"))
        list.add(StationDto("1-Tibbiyot akademiyasi"))
        list.add(StationDto("Mirzo Ulugbek kochasi"))
        list.add(StationDto("Botkin kochasi"))
        list.add(StationDto("S. Mashhadiy kochasi"))
        list.add(StationDto("Shifokorlar malakasi oshirish instituti"))
        list.add(StationDto("M. Ulugbek tuman prokuraturasi"))
        list.add(StationDto("Politologiya instituti"))
        list.add(StationDto("Buyak ipak yoli metrosi"))
        list.add(StationDto("Sayohat mehmonhonasi"))
        list.add(StationDto("Salom kafesi"))
        list.add(StationDto("Mirishkor kochasi"))
        list.add(StationDto("Dormon yoli kochasi"))
        list.add(StationDto("Gulshan bogi"))
        list.add(StationDto("Nevrologiya sanatoriyasi"))
        return list
    }

    fun nextStations(): List<StationDto> {

        val list = ArrayList<StationDto>()

        list.add(StationDto("Toshknet MUM"))
        list.add(StationDto("Foton IChB"))
        list.add(StationDto("Kurantlar bekati"))
        list.add(StationDto("6-tugruxona"))
        list.add(StationDto("Uspenskiy nomli musiqa maktabi"))
        list.add(StationDto("1-Tibbiyot akademiyasi"))
        list.add(StationDto("Mirzo Ulugbek kochasi"))
        list.add(StationDto("Botkin kochasi"))
        list.add(StationDto("S. Mashhadiy kochasi"))
        list.add(StationDto("Shifokorlar malakasi oshirish instituti"))
        list.add(StationDto("M. Ulugbek tuman prokuraturasi"))
        list.add(StationDto("Politologiya instituti"))
        list.add(StationDto("Buyak ipak yoli metrosi"))
        list.add(StationDto("Sayohat mehmonhonasi"))
        list.add(StationDto("Salom kafesi"))
        list.add(StationDto("Mirishkor kochasi"))
        list.add(StationDto("Dormon yoli kochasi"))
        list.add(StationDto("Gulshan bogi"))
        list.add(StationDto("Nevrologiya sanatoriyasi"))
        return list
    }


    data class StationDto(
        val stationName: String = ""
    )
}