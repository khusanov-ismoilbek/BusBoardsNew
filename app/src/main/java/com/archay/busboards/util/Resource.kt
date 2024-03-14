package com.archay.busboards.util

sealed class Resource<out T> {
    data class Success<T>(val data: T, val code: Int) : Resource<T>()
    data class ApiError(val exception: ErrorResponse?, val code: Int?) : Resource<Nothing>()
    data class GenericError(val throwable: Throwable) : Resource<Nothing>()
}