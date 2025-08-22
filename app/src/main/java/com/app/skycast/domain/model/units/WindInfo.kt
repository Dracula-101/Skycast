package com.app.skycast.domain.model.units

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WindInfo (
    val speed: Wind,
    val direction: String,
    val degree: Int
) : Parcelable
