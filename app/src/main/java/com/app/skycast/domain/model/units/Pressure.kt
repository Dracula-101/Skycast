package com.app.skycast.domain.model.units

abstract class Pressure: Unit() {
    abstract val hPa: Float
    abstract val mmHg: Float
}