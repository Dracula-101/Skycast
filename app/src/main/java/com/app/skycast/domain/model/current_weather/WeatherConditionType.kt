package com.app.skycast.domain.model.current_weather

import androidx.annotation.DrawableRes
import com.app.skycast.R

enum class WeatherConditionType {
    SUNNY,
    PARTLY_CLOUDY,
    CLOUDY,
    OVERCAST,
    MIST,
    PATCHY_RAIN_POSSIBLE,
    PATCHY_SNOW_POSSIBLE,
    PATCHY_SLEET_POSSIBLE,
    PATCHY_FREEZING_DRIZZLE_POSSIBLE,
    THUNDERY_OUTBREAKS_POSSIBLE,
    BLOWING_SNOW,
    BLIZZARD,
    FOG,
    FREEZING_FOG,
    PATCHY_LIGHT_DRIZZLE,
    LIGHT_DRIZZLE,
    FREEZING_DRIZZLE,
    HEAVY_FREEZING_DRIZZLE,
    PATCHY_LIGHT_RAIN,
    LIGHT_RAIN,
    MODERATE_RAIN_AT_TIMES,
    MODERATE_RAIN,
    HEAVY_RAIN_AT_TIMES,
    HEAVY_RAIN,
    LIGHT_FREEZING_RAIN,
    MODERATE_OR_HEAVY_FREEZING_RAIN,
    LIGHT_SLEET,
    MODERATE_OR_HEAVY_SLEET,
    PATCHY_LIGHT_SNOW,
    LIGHT_SNOW,
    PATCHY_MODERATE_SNOW,
    MODERATE_SNOW,
    PATCHY_HEAVY_SNOW,
    HEAVY_SNOW,
    ICE_PELLETS,
    LIGHT_RAIN_SHOWER,
    MODERATE_OR_HEAVY_RAIN_SHOWER,
    TORRENTIAL_RAIN_SHOWER,
    LIGHT_SLEET_SHOWERS,
    MODERATE_OR_HEAVY_SLEET_SHOWERS,
    LIGHT_SNOW_SHOWERS,
    MODERATE_OR_HEAVY_SNOW_SHOWERS,
    LIGHT_SHOWERS_OF_ICE_PELLETS,
    MODERATE_OR_HEAVY_SHOWERS_OF_ICE_PELLETS,
    PATCHY_LIGHT_RAIN_WITH_THUNDER,
    MODERATE_OR_HEAVY_RAIN_WITH_THUNDER,
    PATCHY_LIGHT_SNOW_WITH_THUNDER,
    MODERATE_OR_HEAVY_SNOW_WITH_THUNDER;


    override fun toString(): String {
        return when (this) {
            SUNNY -> "Sunny"
            PARTLY_CLOUDY -> "Partly Cloudy"
            CLOUDY -> "Cloudy"
            OVERCAST -> "Overcast"
            MIST -> "Mist"
            PATCHY_RAIN_POSSIBLE -> "Patchy Rain Possible"
            PATCHY_SNOW_POSSIBLE -> "Patchy Snow Possible"
            PATCHY_SLEET_POSSIBLE -> "Patchy Sleet"
            PATCHY_FREEZING_DRIZZLE_POSSIBLE -> "Patchy Freezing Drizzle"
            THUNDERY_OUTBREAKS_POSSIBLE -> "Thundery Outbreaks Possible"
            BLOWING_SNOW -> "Blowing Snow"
            BLIZZARD -> "Blizzard"
            FOG -> "Fog"
            FREEZING_FOG -> "Freezing Fog"
            PATCHY_LIGHT_DRIZZLE -> "Patchy Light Drizzle"
            LIGHT_DRIZZLE -> "Light Drizzle"
            FREEZING_DRIZZLE -> "Freezing Drizzle"
            HEAVY_FREEZING_DRIZZLE -> "Heavy Freezing Drizzle"
            PATCHY_LIGHT_RAIN -> "Patchy Light Rain"
            LIGHT_RAIN -> "Light Rain"
            MODERATE_RAIN_AT_TIMES -> "Moderate Rain at Times"
            MODERATE_RAIN -> "Moderate Rain"
            HEAVY_RAIN_AT_TIMES -> "Heavy Rain at Times"
            HEAVY_RAIN -> "Heavy Rain"
            LIGHT_FREEZING_RAIN -> "Light Freezing Rain"
            MODERATE_OR_HEAVY_FREEZING_RAIN -> "Moderate Freezing Rain"
            LIGHT_SLEET -> "Light Sleet"
            MODERATE_OR_HEAVY_SLEET -> "Moderate Sleet"
            PATCHY_LIGHT_SNOW -> "Patchy Light Snow"
            LIGHT_SNOW -> "Light Snow"
            PATCHY_MODERATE_SNOW -> "Patchy Moderate Snow"
            MODERATE_SNOW -> "Moderate Snow"
            PATCHY_HEAVY_SNOW -> "Patchy Heavy Snow"
            HEAVY_SNOW -> "Heavy Snow"
            ICE_PELLETS -> "Ice Pellets"
            LIGHT_RAIN_SHOWER -> "Light Rain Shower"
            MODERATE_OR_HEAVY_RAIN_SHOWER -> "Moderate Rain Shower"
            TORRENTIAL_RAIN_SHOWER -> "Torrential Rain Shower"
            LIGHT_SLEET_SHOWERS -> "Light Sleet Showers"
            MODERATE_OR_HEAVY_SLEET_SHOWERS -> "Moderate Sleet Showers"
            LIGHT_SNOW_SHOWERS -> "Light Snow Showers"
            MODERATE_OR_HEAVY_SNOW_SHOWERS -> "Moderate Snow Showers"
            LIGHT_SHOWERS_OF_ICE_PELLETS -> "Light Showers of Ice Pellets"
            MODERATE_OR_HEAVY_SHOWERS_OF_ICE_PELLETS -> "Moderate Showers of Ice Pellets"
            PATCHY_LIGHT_RAIN_WITH_THUNDER -> "Patchy Light Rain with Thunder"
            MODERATE_OR_HEAVY_RAIN_WITH_THUNDER -> "Moderate Rain with Thunder"
            PATCHY_LIGHT_SNOW_WITH_THUNDER -> "Patchy Light Snow with Thunder"
            MODERATE_OR_HEAVY_SNOW_WITH_THUNDER -> "Moderate Snow with Thunder"
        }
    }

    @DrawableRes
    fun getDrawableIcon(isDay: Boolean): Int {
        return when(this){
            SUNNY -> if (isDay) R.drawable.ic_w_sunny else R.drawable.ic_w_overcast_night
            PARTLY_CLOUDY -> if (isDay) R.drawable.ic_w_partly_cloudy else R.drawable.ic_w_partly_cloudy_night
            CLOUDY -> if (isDay) R.drawable.ic_w_cloudy else R.drawable.ic_w_cloudy_night
            OVERCAST -> if (isDay) R.drawable.ic_w_overcast else R.drawable.ic_w_overcast_night
            MIST -> if (isDay) R.drawable.ic_w_mist else R.drawable.ic_w_mist_night
            PATCHY_RAIN_POSSIBLE -> if (isDay) R.drawable.ic_w_patchy_rain_possible else R.drawable.ic_w_patchy_rain_possible_night
            PATCHY_SNOW_POSSIBLE -> if (isDay) R.drawable.ic_w_patchy_snow_possible else R.drawable.ic_w_patchy_snow_possible_night
            PATCHY_SLEET_POSSIBLE -> if (isDay) R.drawable.ic_w_patchy_sleet_possible else R.drawable.ic_w_patchy_sleet_possible_night
            PATCHY_FREEZING_DRIZZLE_POSSIBLE -> if (isDay) R.drawable.ic_w_patchy_freezing_drizzle_possible else R.drawable.ic_w_patchy_freezing_drizzle_possible_night
            THUNDERY_OUTBREAKS_POSSIBLE -> if (isDay) R.drawable.ic_w_thundery_outbreaks_possible else R.drawable.ic_w_thundery_outbreaks_possible_night
            BLOWING_SNOW -> if (isDay) R.drawable.ic_w_blowing_snow else R.drawable.ic_w_blowing_snow_night
            BLIZZARD -> if (isDay) R.drawable.ic_w_blizzard else R.drawable.ic_w_blizzard_night
            FOG -> if (isDay) R.drawable.ic_w_fog else R.drawable.ic_w_fog_night
            FREEZING_FOG -> if (isDay) R.drawable.ic_w_freezing_fog else R.drawable.ic_w_freezing_fog_night
            PATCHY_LIGHT_DRIZZLE -> if (isDay) R.drawable.ic_w_patchy_light_drizzle else R.drawable.ic_w_patchy_light_drizzle_night
            LIGHT_DRIZZLE -> if (isDay) R.drawable.ic_w_light_drizzle else R.drawable.ic_w_light_drizzle_night
            FREEZING_DRIZZLE -> if (isDay) R.drawable.ic_w_freezing_drizzle else R.drawable.ic_w_freezing_drizzle_night
            HEAVY_FREEZING_DRIZZLE -> if (isDay) R.drawable.ic_w_heavy_freezing_drizzle else R.drawable.ic_w_heavy_freezing_drizzle_night
            PATCHY_LIGHT_RAIN -> if (isDay) R.drawable.ic_w_patchy_light_rain else R.drawable.ic_w_patchy_light_rain_night
            LIGHT_RAIN -> if (isDay) R.drawable.ic_w_light_rain else R.drawable.ic_w_light_rain_night
            MODERATE_RAIN_AT_TIMES -> if (isDay) R.drawable.ic_w_moderate_rain_at_times else R.drawable.ic_w_moderate_rain_at_times_night
            MODERATE_RAIN -> if (isDay) R.drawable.ic_w_moderate_rain else R.drawable.ic_w_moderate_rain_night
            HEAVY_RAIN_AT_TIMES -> if (isDay) R.drawable.ic_w_heavy_rain_at_times else R.drawable.ic_w_heavy_rain_at_times_night
            HEAVY_RAIN -> if (isDay) R.drawable.ic_w_heavy_rain else R.drawable.ic_w_heavy_rain_night
            LIGHT_FREEZING_RAIN -> if (isDay) R.drawable.ic_w_light_freezing_rain else R.drawable.ic_w_light_freezing_rain_night
            MODERATE_OR_HEAVY_FREEZING_RAIN -> if (isDay) R.drawable.ic_w_moderate_or_heavy_freezing_rain else R.drawable.ic_w_moderate_or_heavy_freezing_rain_night
            LIGHT_SLEET -> if (isDay) R.drawable.ic_w_light_sleet else R.drawable.ic_w_light_sleet_night
            MODERATE_OR_HEAVY_SLEET -> if (isDay) R.drawable.ic_w_moderate_or_heavy_sleet else R.drawable.ic_w_moderate_or_heavy_sleet_night
            PATCHY_LIGHT_SNOW -> if (isDay) R.drawable.ic_w_patchy_light_snow else R.drawable.ic_w_patchy_light_snow_night
            LIGHT_SNOW -> if (isDay) R.drawable.ic_w_light_snow else R.drawable.ic_w_light_snow_night
            PATCHY_MODERATE_SNOW -> if (isDay) R.drawable.ic_w_patchy_moderate_snow else R.drawable.ic_w_patchy_moderate_snow_night
            MODERATE_SNOW -> if (isDay) R.drawable.ic_w_moderate_snow else R.drawable.ic_w_moderate_snow_night
            PATCHY_HEAVY_SNOW -> if (isDay) R.drawable.ic_w_patchy_heavy_snow else R.drawable.ic_w_patchy_heavy_snow_night
            HEAVY_SNOW -> if (isDay) R.drawable.ic_w_heavy_snow else R.drawable.ic_w_heavy_snow_night
            ICE_PELLETS -> if (isDay) R.drawable.ic_w_ice_pellets else R.drawable.ic_w_ice_pellets_night
            LIGHT_RAIN_SHOWER -> if (isDay) R.drawable.ic_w_light_rain_shower else R.drawable.ic_w_light_rain_shower_night
            MODERATE_OR_HEAVY_RAIN_SHOWER -> if (isDay) R.drawable.ic_w_moderate_or_heavy_rain_shower else R.drawable.ic_w_moderate_or_heavy_rain_shower_night
            TORRENTIAL_RAIN_SHOWER -> if (isDay) R.drawable.ic_w_torrential_rain_shower else R.drawable.ic_w_torrential_rain_shower_night
            LIGHT_SLEET_SHOWERS -> if (isDay) R.drawable.ic_w_light_sleet_showers else R.drawable.ic_w_light_sleet_showers_night
            MODERATE_OR_HEAVY_SLEET_SHOWERS -> if (isDay) R.drawable.ic_w_moderate_or_heavy_sleet_showers else R.drawable.ic_w_moderate_or_heavy_sleet_showers_night
            LIGHT_SNOW_SHOWERS -> if (isDay) R.drawable.ic_w_light_snow_showers else R.drawable.ic_w_light_snow_showers_night
            MODERATE_OR_HEAVY_SNOW_SHOWERS -> if (isDay) R.drawable.ic_w_moderate_or_heavy_snow_showers else R.drawable.ic_w_moderate_or_heavy_snow_showers_night
            LIGHT_SHOWERS_OF_ICE_PELLETS -> if (isDay) R.drawable.ic_w_light_showers_of_ice_pellets else R.drawable.ic_w_light_showers_of_ice_pellets_night
            MODERATE_OR_HEAVY_SHOWERS_OF_ICE_PELLETS -> if (isDay) R.drawable.ic_w_moderate_or_heavy_showers_of_ice_pellets else R.drawable.ic_w_moderate_or_heavy_showers_of_ice_pellets_night
            PATCHY_LIGHT_RAIN_WITH_THUNDER -> if (isDay) R.drawable.ic_w_patchy_light_rain_with_thunder else R.drawable.ic_w_patchy_light_rain_with_thunder_night
            MODERATE_OR_HEAVY_RAIN_WITH_THUNDER -> if (isDay) R.drawable.ic_w_moderate_or_heavy_rain_with_thunder else R.drawable.ic_w_moderate_or_heavy_rain_with_thunder_night
            PATCHY_LIGHT_SNOW_WITH_THUNDER -> if (isDay) R.drawable.ic_w_patchy_light_snow_with_thunder else R.drawable.ic_w_patchy_light_snow_with_thunder_night
            MODERATE_OR_HEAVY_SNOW_WITH_THUNDER -> if (isDay) R.drawable.ic_w_moderate_or_heavy_snow_with_thunder else R.drawable.ic_w_moderate_or_heavy_snow_with_thunder_night
        }
    }

    companion object {
        private val codeMap = mapOf(
            1000 to SUNNY,
            1003 to PARTLY_CLOUDY,
            1006 to CLOUDY,
            1009 to OVERCAST,
            1030 to MIST,
            1063 to PATCHY_RAIN_POSSIBLE,
            1066 to PATCHY_SNOW_POSSIBLE,
            1069 to PATCHY_SLEET_POSSIBLE,
            1072 to PATCHY_FREEZING_DRIZZLE_POSSIBLE,
            1087 to THUNDERY_OUTBREAKS_POSSIBLE,
            1114 to BLOWING_SNOW,
            1117 to BLIZZARD,
            1135 to FOG,
            1147 to FREEZING_FOG,
            1150 to PATCHY_LIGHT_DRIZZLE,
            1153 to LIGHT_DRIZZLE,
            1168 to FREEZING_DRIZZLE,
            1171 to HEAVY_FREEZING_DRIZZLE,
            1180 to PATCHY_LIGHT_RAIN,
            1183 to LIGHT_RAIN,
            1186 to MODERATE_RAIN_AT_TIMES,
            1189 to MODERATE_RAIN,
            1192 to HEAVY_RAIN_AT_TIMES,
            1195 to HEAVY_RAIN,
            1198 to LIGHT_FREEZING_RAIN,
            1201 to MODERATE_OR_HEAVY_FREEZING_RAIN,
            1204 to LIGHT_SLEET,
            1207 to MODERATE_OR_HEAVY_SLEET,
            1210 to PATCHY_LIGHT_SNOW,
            1213 to LIGHT_SNOW,
            1216 to PATCHY_MODERATE_SNOW,
            1219 to MODERATE_SNOW,
            1222 to PATCHY_HEAVY_SNOW,
            1225 to HEAVY_SNOW,
            1237 to ICE_PELLETS,
            1240 to LIGHT_RAIN_SHOWER,
            1243 to MODERATE_OR_HEAVY_RAIN_SHOWER,
            1246 to TORRENTIAL_RAIN_SHOWER,
            1249 to LIGHT_SLEET_SHOWERS,
            1252 to MODERATE_OR_HEAVY_SLEET_SHOWERS,
            1255 to LIGHT_SNOW_SHOWERS,
            1258 to MODERATE_OR_HEAVY_SNOW_SHOWERS,
            1261 to LIGHT_SHOWERS_OF_ICE_PELLETS,
            1264 to MODERATE_OR_HEAVY_SHOWERS_OF_ICE_PELLETS,
            1273 to PATCHY_LIGHT_RAIN_WITH_THUNDER,
            1276 to MODERATE_OR_HEAVY_RAIN_WITH_THUNDER,
            1279 to PATCHY_LIGHT_SNOW_WITH_THUNDER,
            1282 to MODERATE_OR_HEAVY_SNOW_WITH_THUNDER
        )

        fun fromCode(code: Int): WeatherConditionType {
            return codeMap[code] ?: SUNNY
        }

        fun fromString(string: String): WeatherConditionType {
            return when (string) {
                "snow" -> HEAVY_SNOW
                "rain" -> HEAVY_RAIN
                "fog" -> FOG
                "wind" -> BLOWING_SNOW
                "cloudy" -> CLOUDY
                "partly-cloudy-day", "partly-cloudy-night" -> PARTLY_CLOUDY
                "clear-day", "clear-night" -> OVERCAST
                "snow-showers-day", "snow-showers-night" -> LIGHT_SNOW_SHOWERS
                "thunder-rain" -> THUNDERY_OUTBREAKS_POSSIBLE
                "thunder-showers-day", "thunder-showers-night" -> THUNDERY_OUTBREAKS_POSSIBLE
                "showers-day", "showers-night" -> LIGHT_RAIN_SHOWER
                else -> SUNNY
            }
        }
    }
}