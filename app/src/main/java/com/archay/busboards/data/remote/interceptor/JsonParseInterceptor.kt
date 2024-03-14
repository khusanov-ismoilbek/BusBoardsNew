//package com.example.sample.data.remote.interceptor
//
//import android.util.Log
//import com.example.sample.BuildConfig
//import com.example.sample.data.remote.exceptions.FakeTimeException
//import com.example.sample.data.remote.exceptions.ServerErrorException
//import com.example.sample.local.BusPreferences
//import okhttp3.Interceptor
//import okhttp3.RequestBody
//import okhttp3.Response
//import okhttp3.ResponseBody
//import okio.Buffer
//import okio.IOException
//import org.json.JSONObject
//
//class JsonParseInterceptor(private val preferences: BusPreferences) : Interceptor {
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request().newBuilder()
////        val mediaType = MediaType.parse("application/json")
////        val body: RequestBody? = RequestBody.create(mediaType, bodyToString(chain.request().body()))
//
////        request.header("X-App-Version", BuildConfig.VERSION_NAME)
////        request.header("Content-Type", "application/json")
////        if (preferences.deviceId.isNotEmpty())
////            request.header("X-Device-Id", preferences.deviceId)
////        if (preferences.accessToken.isNotEmpty())
////            request.header("Authorization", "Bearer " + preferences.accessToken)
//
//        try {
//            val response = chain.proceed(request.build())
//            val responseBody: String? = response.body()?.string()
//
//            if (BuildConfig.DEBUG)
//                printTlsAndCipherSuiteInfo(response)
//
//            if (response.isSuccessful) {
//                val responseBodyJson = responseBody!!
//                val jsonObject = JSONObject(responseBodyJson)
//
//                val contentType = response.body()?.contentType()
//
//                val newBody = ResponseBody.create(contentType, jsonObject.toString())
//                return response.newBuilder().body(newBody).build()
//            } else {
//                throw ServerErrorException()
//            }
//        } catch (ex: Exception) {
//            Log.e("TTT", "catch ex: ${ex.message}")
//            throw FakeTimeException()
//        }
//    }
//
//    private fun bodyToString(request: RequestBody?): String {
//        return try {
//            val copy: RequestBody? = request
//            val buffer = Buffer()
//            if (copy != null) copy.writeTo(buffer) else return ""
//            buffer.readUtf8()
//        } catch (e: IOException) {
//            "did not work"
//        }
//    }
//
//    private fun printTlsAndCipherSuiteInfo(response: Response?) {
//        if (response != null) {
//            val handshake = response.handshake()
//            if (handshake != null) {
//                val cipherSuite = handshake.cipherSuite()
//                val tlsVersion = handshake.tlsVersion()
//                Log.d("OkHttp3-SSLHandshake", "\"TLS: $tlsVersion, CipherSuite: $cipherSuite\"")
//            }
//        }
//    }
//
//}