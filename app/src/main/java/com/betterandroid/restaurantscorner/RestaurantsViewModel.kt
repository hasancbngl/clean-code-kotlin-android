package com.betterandroid.restaurantscorner

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