package com.archay.busboards.data.remote.response

import com.archay.busboards.data.dto.DeviceByTokenDto
import com.squareup.moshi.Json

data class DeviceByTokenResponse(
    @Json(name = "id")
    val id: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "isConfirmed")
    val isConfirmed: Boolean?,
    @Json(name = "token")
    val token: String?,
    @Json(name = "collection")
    val collection: String?,
    @Json(name = "owner")
    val owner: String?
) {
    fun mapToDto(): DeviceByTokenDto = DeviceByTokenDto(
        id = id ?: "",
        name = name ?: "",
        description = description ?: "",
        isConfirmed = isConfirmed ?: false,
        token = token ?: "",
        collection = collection ?: "",
        owner = owner ?: ""
    )
}
