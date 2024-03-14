package com.archay.busboards.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdsDto(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val uploadDate: String = "",
    val isFullscreen: Boolean = false,
    val owner: Int = 0,
//    val collections: List<Collections?>
) : Parcelable
