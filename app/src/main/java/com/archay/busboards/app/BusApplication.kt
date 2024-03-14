package com.archay.busboards.app

import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
@UnstableApi
class BusApplication : MultiDexApplication() {

    companion object {
        lateinit var cache: Cache
    }

    private lateinit var exoCache: Cache
    private val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
    private lateinit var downloadDirectory: File
    private lateinit var databaseProvider: DatabaseProvider

    override fun onCreate() {
        super.onCreate()

        cache = getExoCache(applicationContext)
    }

    @Synchronized
    private fun getExoCache(context: Context): Cache {
        if (!::exoCache.isInitialized) {
            val exoCacheDirectory =
                File(getExoCacheDirectory(context), DOWNLOAD_CONTENT_DIRECTORY)
            val evict = LeastRecentlyUsedCacheEvictor((getTotalExternalMemorySize() * 0.5).toLong())
            exoCache = SimpleCache(
                exoCacheDirectory,
                evict,
                getDatabaseProvider(context)
            )
        }
        return exoCache
    }

    @Synchronized
    private fun getExoCacheDirectory(context: Context): File {
        if (!::downloadDirectory.isInitialized) {
            downloadDirectory = context.getExternalFilesDir(null) ?: context.filesDir
        }
        return downloadDirectory
    }

    @Synchronized
    private fun getDatabaseProvider(context: Context): DatabaseProvider {
        if (!::databaseProvider.isInitialized) databaseProvider =
            StandaloneDatabaseProvider(context)
        return databaseProvider
    }


    private fun getTotalExternalMemorySize(): Long {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long = stat.blockSizeLong
        val totalBlocks: Long = stat.blockCountLong
        return ((totalBlocks * blockSize))
    }
}