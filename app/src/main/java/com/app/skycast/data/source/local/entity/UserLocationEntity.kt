package com.app.skycast.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_location")
data class UserLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val city: String,
    val country: String,
    val state: String,
    val latitude: Double,
    val longitude: Double,
    val countryCode: String,
    val isFavorite: Boolean,
)