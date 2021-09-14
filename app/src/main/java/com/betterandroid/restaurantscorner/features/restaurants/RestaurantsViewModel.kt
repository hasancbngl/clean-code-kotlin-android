package com.betterandroid.restaurantscorner.features.restaurants

import com.betterandroid.restaurantscorner.domain.models.restaurants.Restaurant
import com.betterandroid.restaurantscorner.domain.models.restaurants.RestaurantDisplayItem
import com.betterandroid.restaurantscorner.domain.models.restaurants.RestaurantType

object RestaurantsViewModel {

    fun getDisplayRestaurants(restaurants: List<Restaurant>) = restaurants.map {
        return@map RestaurantDisplayItem(
            id = it.id,
            displayName = "Restaurant ${it.name}",
            displayDistance = "at ${it.distance} KM distance",
            imageUrl = it.imageUrl,
            type = when (it.type) {
                "EAT_IN" -> RestaurantType.EAT_IN
                "TAKE_AWAY" -> RestaurantType.TAKE_AWAY
                else -> RestaurantType.DRIVE_THROUGH
            }
        )
    }
}