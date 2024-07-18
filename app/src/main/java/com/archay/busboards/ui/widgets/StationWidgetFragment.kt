package com.archay.busboards.ui.widgets

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.archay.busboards.base.BaseFragment
import com.archay.busboards.data.remote.response.WeatherResponse
import com.archay.busboards.data.remote.service.WeatherService
import com.archay.busboards.databinding.WidgetStationBinding
import com.archay.busboards.ui.BusViewModel
import com.archay.busboards.ui.widgets.adapter.StationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class StationWidgetFragment : BaseFragment<WidgetStationBinding>(WidgetStationBinding::inflate) {

    private val viewModel by viewModels<BusViewModel>()

    private val stationAdapter by lazy { StationAdapter() }

    private var list = ArrayList<BusViewModel.StationDto>()
    private var firstItem = BusViewModel.StationDto()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        list.addAll(viewModel.nextStations())


        binding.rvStation.adapter = stationAdapter
        stationAdapter.submitList(updateStation())

        val currentTime = System.currentTimeMillis()
        binding.tvDate.text = SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.getDefault()
        ).format(
            Date(currentTime)
        )


        updateTime()

        lifecycleScope.launch(Dispatchers.IO) {
            getWeather()
        }
    }

    private fun updateTime() {
        val currentTime = Calendar.getInstance().time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(currentTime)
        binding.tvTime.text = formattedTime
        lifecycleScope.launch {
            delay(60_000)
            updateTime()
        }
    }

    private fun getDayOfWeek(currentTimeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTimeMillis

        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> "Invalid day"
        }
    }

    private fun updateStation(): List<BusViewModel.StationDto> {

        lifecycleScope.launch {
            delay(3_000)

            firstItem = list[0]
            list.removeAt(0)
            list.add(list.size, firstItem)

            stationAdapter.submitList(list)
            stationAdapter.notifyDataSetChanged()

            updateStation()
        }

        return list
    }

    private fun getWeather() {
        val api = Retrofit.Builder()
            .baseUrl("http://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

        api.getWeather(
            key = "e90afe298af845c08ae233929242303",
            q = "Uzbekistan",
            days = 1
        ).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        requireActivity().runOnUiThread {
                            binding.tvCurrentWeather.text =
                                it.current?.feelslike_c.toString() + "°C"

                            Log.i("TTTT", "icon: ${it.current?.condition?.icon}")

                            if (it.forecast?.forecastday?.isEmpty() == true) {
                                val day = it.forecast.forecastday
                                binding.tvMinMax.text =
                                    day[0]?.mintemp_c.toString() + " / " + day[0]?.maxtemp_c.toString()
                            }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("TTTT", "onFailure: ${t.message}")
            }

        })
    }

}