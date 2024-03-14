package com.archay.busboards.util.exoplayer

import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi
object ExoplayerUtil {

    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var httpDataSourceFactory: DefaultHttpDataSource.Factory


//    @Synchronized
    fun cacheDataSourceFactory(context: Context, exoCache: Cache): DataSource.Factory {
//        val upstreamFactory = DefaultDataSource.Factory(
//            requireContext(),
//            getHttpDataSourceFactory(requireContext())
//        )
        if (!::dataSourceFactory.isInitialized)

            dataSourceFactory = CacheDataSource.Factory()
                .setCache(exoCache)
                .setUpstreamDataSourceFactory(getHttpDataSourceFactory(context))
                .setCacheReadDataSourceFactory(FileDataSource.Factory())
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
                .setCacheWriteDataSinkFactory {
                    CacheDataSink(
                        exoCache, CacheDataSink.DEFAULT_FRAGMENT_SIZE
                    )
                }
                .setEventListener(object : CacheDataSource.EventListener {
                    override fun onCachedBytesRead(cacheSizeBytes: Long, cachedBytesRead: Long) {

                    }

                    override fun onCacheIgnored(reason: Int) {

                    }

                })

        return dataSourceFactory
    }

//    @Synchronized
    private fun getHttpDataSourceFactory(context: Context): DefaultHttpDataSource.Factory {
        if (!::httpDataSourceFactory.isInitialized) {
            httpDataSourceFactory =
                DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
//                    .setUserAgent()
//                    .setConnectTimeoutMs()
//                    .setReadTimeoutMs()
//                    .setKeepPostFor302Redirects() TODO Each method should be read and studied and add them
        }
        return httpDataSourceFactory
    }
}