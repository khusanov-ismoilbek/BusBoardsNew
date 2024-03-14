package com.archay.busboards.data.di

import android.content.Context
import com.archay.busboards.BuildConfig
import com.archay.busboards.data.remote.service.BusService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val API_VERSION = 1

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
//        authenticator: TokenAuthenticator
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(100L, TimeUnit.SECONDS)
            .readTimeout(50L, TimeUnit.SECONDS)
            .writeTimeout(50L, TimeUnit.SECONDS)
//            .addInterceptor(interceptor)

        if (com.archay.busboards.BuildConfig.DEBUG) {
//            val logging = HttpLoggingInterceptor()
//            logging.level = HttpLoggingInterceptor.Level.BODY
//            clientBuilder.addInterceptor(logging)
//            clientBuilder.addInterceptor(ChuckerInterceptor(context))
        }

        val okHttpClient = clientBuilder.build()

        return okHttpClient
    }

//    @Singleton
//    @Provides
//    fun provideJsonMainInterceptor(
//        preferences: BusPreferences,
//    ): JsonParseInterceptor = JsonParseInterceptor(preferences)

    @Singleton
    @Provides
    fun provideMoshiConvertor(): MoshiConverterFactory {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return MoshiConverterFactory.create(moshi).asLenient()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL /*+ "v" + API_VERSION + "/"*/)
            .client(okHttpClient)
            .addConverterFactory(moshiFactory)
            .build()
    }


    @Singleton
    @Provides
    fun provideBusService(retrofit: Retrofit): BusService {
        return retrofit.create(BusService::class.java)
    }
}