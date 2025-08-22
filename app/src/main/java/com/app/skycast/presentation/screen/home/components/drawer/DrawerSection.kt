package com.app.skycast.presentation.screen.home.components.drawer


enum class DrawerSection {
    Forecast,
    Locations,
    News,
    AqiInfo;

    override fun toString(): String {
        return when(this) {
            Forecast -> "Forecast"
            Locations -> "Locations"
            News -> "News"
            AqiInfo -> "AQI Info"
        }
    }
}