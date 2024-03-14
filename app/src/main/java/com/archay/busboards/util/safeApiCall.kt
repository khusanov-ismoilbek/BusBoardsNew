package com.archay.busboards.util

import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException

fun <T> safeApiCall(
    dispatchersProvider: CoroutineDispatcher,
    apiCall: suspend () -> BaseResponseWrapper<T>
): Flow<Resource<T>> = flow {
    emit(checkStatus(apiCall.invoke()))
}.catch {
    if (it is IOException) {
        emit(Resource.GenericError(Exception(Throwable(message = "network error"))))
    } else {
        emit(Resource.GenericError(it))
    }
}.flowOn(dispatchersProvider)

fun safeApiCallFile(
    dispatchersProvider: CoroutineDispatcher,
    apiCall: suspend () -> Response<ResponseBody>
): Flow<Resource<ResponseBody>> = flow {
    emit(checkStatusFile(apiCall.invoke()))
}.catch {
    if (it is IOException) {
        emit(Resource.GenericError(Exception(Throwable(message = "network error"))))
    } else {
        emit(Resource.GenericError(it))
    }
}.flowOn(dispatchersProvider)


private fun <T> checkStatusFile(
    response: Response<T>
): Resource<T> {
    val body = response.body()

    val data: T? = body
    return if (response.isSuccessful) {
        return if (data != null) {
            Resource.Success(data, response.code())
        } else {
            Resource.GenericError(Exception(Throwable(message = "loading error data")))
        }
    } else {
        Resource.ApiError(
            convertErrorBody(response.errorBody()),
            response.code()
        )
    }
}

private fun <T> checkStatus(
    response: BaseResponseWrapper<T>
): Resource<T> {
    val body = response.body()

    val data: T? = body?.data
    return if (response.isSuccessful) {
        return if (data != null) {
            Resource.Success(data, response.code())
        } else {
            Resource.GenericError(Exception(Throwable(message = "loading error data")))
        }
    } else {
        Resource.ApiError(
            convertErrorBody(response.errorBody()),
            response.code()
        )
    }
}


fun convertErrorBody(errorBody: ResponseBody?): ErrorResponse? {
    return try {
        errorBody?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}