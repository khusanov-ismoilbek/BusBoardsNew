package com.archay.busboards.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceByTokenDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val isConfirmed: Boolean = false,
    val token: String = "",
    val collection: String = "",
    val owner: String = ""
) : Parcelable
