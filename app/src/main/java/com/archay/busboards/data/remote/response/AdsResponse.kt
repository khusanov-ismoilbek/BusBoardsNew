package com.archay.busboards.data.remote.response

import com.archay.busboards.data.dto.AdsDto
import com.squareup.moshi.Json

data class AdsResponse(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "video_file")
    val videoUrl: String?,
//    @Json(name = "upload_date")
//    val uploadDate: String?,
//    @Json(name = "isFullscreen")
//    val isFullscreen: Boolean?,
//    @Json(name = "owner")
//    val owner: Int?,
//    @Json(name = "collections")
//    val collections: List<Collections?>
) {
    fun mapToDto(): AdsDto = AdsDto(
        id = id ?: 0,
        title = title ?: "",
        description = description ?: "",
        videoUrl = videoUrl ?: "",
//        uploadDate = uploadDate ?: "",
//        isFullscreen = isFullscreen ?: false,
//        owner = owner ?: 0
    )
}

data class Collections(
    val id: Int
)