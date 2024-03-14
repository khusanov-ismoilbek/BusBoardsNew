package com.archay.busboards.util

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class ErrorResponse(
    @field:Json(name = "errorMessage")
    val errorMessage: String? ,
    @field:Json(name = "code")
    val code: String? = ""
)