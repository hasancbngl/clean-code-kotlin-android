package com.betterandroid.restaurantscorner

enum class RestaurantType(
    val text: String,
    val drawableId: Int,
    val colorId: Int
) {
    TAKE_AWAY("Take away", R.drawable.take_away, R.color.orange),
    EAT_IN("Eat In", R.drawable.eat_in, R.color.brown),
    DRIVE_THROUGH("Drive T", R.drawable.drive_through, R.color.colorAccent)
}