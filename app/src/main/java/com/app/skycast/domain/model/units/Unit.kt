package com.app.skycast.domain.model.units

abstract class Unit {

    abstract val metric: Float
    abstract val imperial: Float

    abstract fun display(unit: UnitType, addSpace: Boolean = true): String

    fun getValue(unit: UnitType): Float {
        return when (unit) {
            UnitType.METRIC -> metric
            UnitType.IMPERIAL -> imperial
        }
    }
}