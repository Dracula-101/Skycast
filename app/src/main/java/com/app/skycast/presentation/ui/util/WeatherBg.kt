package com.app.skycast.presentation.ui.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.app.skycast.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class WeatherBg {
    PRE_SUNRISE,
    SUNRISE,
    POST_SUNRISE,
    PRE_MORNING,
    MORNING,
    AFTERNOON,
    EVENING,
    SUNSET,
    NIGHT,
    PRE_MID_NIGHT,
    MID_NIGHT;

    fun getDominantColor(): Color {
        return when (this) {
            SUNRISE -> Color(0xff006090)
            POST_SUNRISE -> Color(0xffd05878)
            PRE_MORNING -> Color(0xff602048)
            MORNING -> Color(0xff0880d0)
            AFTERNOON -> Color(0xfff88088)
            EVENING -> Color(0xfff0e0e8)
            SUNSET -> Color(0xffd098d0)
            NIGHT -> Color(0xff000848)
            PRE_MID_NIGHT -> Color(0xff101038)
            MID_NIGHT -> Color(0xff181820)
            PRE_SUNRISE -> Color(0x82ffffff) // Default color for PRE_SUNRISE
        }
    }

    fun getTitleTextColor(): Color {
        return when (this) {
            SUNRISE -> Color(0x86ffffff)
            POST_SUNRISE -> Color(0x8f000000)
            PRE_MORNING -> Color(0x63ffffff)
            MORNING -> Color(0x94000000)
            AFTERNOON -> Color(0x7a000000)
            EVENING -> Color(0x6d000000)
            SUNSET -> Color(0x79000000)
            NIGHT -> Color(0x5affffff)
            PRE_MID_NIGHT -> Color(0x57ffffff)
            MID_NIGHT -> Color(0x55ffffff)
            PRE_SUNRISE -> Color(0x86ffffff) // Default color for PRE_SUNRISE
        }
    }

    fun getBodyTextColor(): Color {
        return when (this) {
            SUNRISE -> Color(0xbeffffff)
            POST_SUNRISE -> Color(0xcc000000)
            PRE_MORNING -> Color(0x8bffffff)
            MORNING -> Color(0xda000000)
            AFTERNOON -> Color(0xa2000000)
            EVENING -> Color(0x8d000000)
            SUNSET -> Color(0xa0000000)
            NIGHT -> Color(0x77ffffff)
            PRE_MID_NIGHT -> Color(0x75ffffff)
            MID_NIGHT -> Color(0x74ffffff)
            PRE_SUNRISE -> Color(0x82ffffff) // Default color for PRE_SUNRISE
        }
    }

    fun getBottomColor(): Color {
        return when (this) {
            PRE_SUNRISE -> Color(0xff020b35)
            SUNRISE -> Color(0xff55b2c2)
            POST_SUNRISE -> Color(0xff0e0832)
            PRE_MORNING -> Color(0xff000105)
            MORNING -> Color(0xff01112a)
            AFTERNOON -> Color(0xff293446)
            EVENING -> Color(0xff422544)
            SUNSET -> Color(0xff270635)
            NIGHT -> Color(0xff0f1324)
            PRE_MID_NIGHT -> Color(0xff001322)
            MID_NIGHT -> Color(0xff080a13)
        }
    }

    @DrawableRes
    fun toBackground(): Int {
        return when(this){
            PRE_SUNRISE -> R.drawable.pre_sunrise_bg
            SUNRISE -> R.drawable.sunrise_bg
            POST_SUNRISE -> R.drawable.post_sunrise_bg
            PRE_MORNING -> R.drawable.pre_morning_bg
            MORNING -> R.drawable.morning_bg
            AFTERNOON -> R.drawable.afternoon_bg
            EVENING -> R.drawable.evening_bg
            SUNSET -> R.drawable.sunset_bg
            NIGHT -> R.drawable.night_bg
            PRE_MID_NIGHT -> R.drawable.pre_mid_night_bg
            MID_NIGHT -> R.drawable.mid_night_bg
        }
    }

    companion object {
        fun fromDate(date: Date): WeatherBg {
            val hour = SimpleDateFormat("HH", Locale.getDefault()).format(date).toInt()
            return when (hour) {
                in 0..5 -> PRE_MID_NIGHT
                in 6..7 -> PRE_SUNRISE
                in 8..9 -> SUNRISE
                in 10..11 -> POST_SUNRISE
                in 12..13 -> PRE_MORNING
                in 14..15 -> MORNING
                in 16..17 -> AFTERNOON
                in 18..19 -> EVENING
                in 20..21 -> SUNSET
                in 22..23 -> NIGHT
                else -> MID_NIGHT
            }
        }
    }
}
