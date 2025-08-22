package com.app.skycast.domain.repository.user.config

import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.units.UnitType
import kotlinx.coroutines.flow.StateFlow

abstract class UserConfigRepository {

    abstract fun getUserLocationFlow(): StateFlow<UserLocation?>

    abstract fun getUserLocation(): UserLocation?

    abstract fun setUserLocation(userLocation: UserLocation)

    abstract fun hasUserSetLocation(): StateFlow<Boolean>

    abstract fun clearUserLocation()

    abstract fun getUserMeasurementUnit(): UnitType

    abstract fun setUserMeasurementUnit(unitType: UnitType)

    abstract fun userMeasurementUnitFlow(): StateFlow<UnitType>

    abstract fun clearUserMeasurementUnit()

}

