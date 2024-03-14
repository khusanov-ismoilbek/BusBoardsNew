package com.archay.busboards.util

import com.squareup.moshi.Json
import retrofit2.Response

typealias BaseResponseWrapper<T> = Response<BaseResponse<T>>
data class BaseResponse<T>(
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "data")
    val data: T,
    @field:Json(name = "timestamp")
    val timestamp: Long?,
    @field:Json(name = "errorMessage")
    val errorMessage: String?
)