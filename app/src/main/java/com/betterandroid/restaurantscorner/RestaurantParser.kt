package com.betterandroid.restaurantscorner

object RestaurantParser {
    fun parseRestaurants(response: RestaurantListResponse): List<Restaurant> {
        return response.restaurants
            ?.filter {
                it.name != null && it.imageUrl != null
            }?.map {
                return@map Restaurant(
                    id = it.id,
                    name = it.name!!,
                    imageUrl = it.imageUrl!!,
                    location = SimpleLocation(it.locationLatitude, it.locationLongitude),
                    closingHour = it.closingHour,
                    type = it.type
                )
            }.orEmpty()
        //if couldnt filter it and its null return empty
    }
}