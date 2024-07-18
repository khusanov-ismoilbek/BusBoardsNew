package com.archay.busboards.data.remote.response

data class WeatherResponse(
    val current: Current?,
    val forecast: Forecast?
)

data class Current(
    val feelslike_c: Float?,
    val condition: Condition?
)

data class Condition(
    val icon: String?
)

data class Forecast(
    val forecastday: List<Day?>?
)

data class Day(
    val maxtemp_c: Float?,
    val mintemp_c: Float?
)