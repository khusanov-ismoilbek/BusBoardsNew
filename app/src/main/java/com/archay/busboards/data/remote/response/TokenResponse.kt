package com.archay.busboards.data.remote.response

import com.archay.busboards.data.dto.TokenDto
import com.squareup.moshi.Json

data class TokenResponse(
    @Json(name = "token")
    val token: String?
) {
    fun mapToDto(): TokenDto = TokenDto(token = token ?: "")
}
