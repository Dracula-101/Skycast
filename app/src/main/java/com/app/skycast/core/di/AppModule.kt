package com.app.skycast.core.di

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.nio.file.spi.FileSystemProvider
import java.util.concurrent.Executor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ======================== Main Executor ========================
    @Provides
    @Singleton
    fun provideMainExecutor(@ApplicationContext cxt: Context): Executor {
        return ContextCompat.getMainExecutor(cxt)
    }

    // ======================== Content Resolver ========================
    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    // ======================== Package Manager ========================
    @Provides
    @Singleton
    fun providePackageManager(@ApplicationContext context: Context): PackageManager {
        return context.packageManager
    }

    // ======================== Audio Manager ========================
    @Provides
    @Singleton
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager {
        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }


    // ======================== File System Provider ========================
    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideFileSystemProvider(): FileSystemProvider {
        return FileSystemProvider.installedProviders().first()
    }

    // ======================== Local Files Directory ========================
    @Provides
    @Singleton
    @Named("files")
    fun provideLocalFiles(@ApplicationContext context: Context): File {
        return context.filesDir
    }

    // ======================== Local Cache Directory ========================
    @Provides
    @Singleton
    @Named("cache")
    fun provideLocalCache(@ApplicationContext context: Context): File {
        return context.cacheDir
    }

    // ======================== Asset Manager ========================
    @Provides
    @Singleton
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager {
        return context.assets
    }

}