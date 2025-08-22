package com.app.skycast.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.app.skycast.data.source.local.entity.UserLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserLocationDao {

    @Upsert(entity = UserLocationEntity::class)
    suspend fun insertUserLocation(userLocation: UserLocationEntity): Long

    @Update(entity = UserLocationEntity::class)
    suspend fun updateUserLocation(userLocation: UserLocationEntity): Int

    @Query("SELECT * FROM user_location WHERE isFavorite = 1")
    fun getFavouriteUserLocation(): Flow<List<UserLocationEntity>>

    @Query("UPDATE user_location SET isFavorite = :isFavourite WHERE city = :city")
    suspend fun updateFavouriteUserLocation(city: String, isFavourite: Boolean): Int

    @Query("SELECT * FROM user_location WHERE city = :city")
    suspend fun getUserLocation(city: String): UserLocationEntity?

    @Query("SELECT * FROM user_location")
    fun getAllUserLocations(): Flow<List<UserLocationEntity>>

    @Query("DELETE FROM user_location WHERE city = :city")
    suspend fun deleteUserLocation(city: String): Int
}