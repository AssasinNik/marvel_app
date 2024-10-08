package com.example.marvel_app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MarvelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
    class MarvelApplication : Application(), ImageLoaderFactory {
        override fun newImageLoader(): ImageLoader {
            return ImageLoader.Builder(this)
                .memoryCache {
                    MemoryCache.Builder(this)
                        .maxSizePercent(0.1)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .maxSizePercent(0.03)
                        .directory(cacheDir.resolve("image_cache"))
                        .maxSizeBytes(5 * 1024 * 1024)
                        .build()
                }
                .logger(DebugLogger())
                .respectCacheHeaders(false)
                .build()
        }
    }
}