package com.app.skycast.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.skycast.data.source.local.dao.UserLocationDao
import com.app.skycast.data.source.local.entity.UserLocationEntity

@Database(
    entities = [
        UserLocationEntity::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userLocationDao(): UserLocationDao
}