package com.betterandroid.restaurantscorner

import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.betterandroid.restaurantscorner.mocks.MockCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_restaurants.*
import java.util.*

class RestaurantsActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()
    private var restaurantsAdapter: RestaurantsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)
        restaurantsAdapter = RestaurantsAdapter()
        recyclerViewRestaurants.apply {
            layoutManager = LinearLayoutManager(
                context!!,
                LinearLayoutManager.VERTICAL,
                false
            )
            this.adapter = restaurantsAdapter
        }
        showRestaurants()
    }

    private fun showRestaurants() {
        getRestaurants { response ->
            //parsing, filtering, displaying
            val parsedRestaurants = parseRestaurants(response)
            val filteredRestaurants = filterRestaurants(parsedRestaurants)
            displayRestaurants(filteredRestaurants)
        }
    }

    private fun getRestaurants(completionHandler: (response: RestaurantListResponse) -> Unit) {
        val client = RestaurantsRestClient()
        val userId = MockCreator.getUserId()
        disposable.add(
            client.getRestaurants(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> completionHandler.invoke(response) }, {})
        )
    }

    private fun displayRestaurants(restaurants: List<Restaurant>) {
        //find the restaurant where the distance is 50
        val foundRestaurant = restaurants.find { it.distance ==50 }

        val displayRestaurants = restaurants.map {
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

        val adapter = restaurantsAdapter
        if (adapter != null) {
            adapter.restaurants = displayRestaurants
            adapter.clickListener =
                object : RestaurantsAdapter.RestaurantClickListener {
                    override fun onRestaurantClicked(restaurantId: Int) {
                        Toast.makeText(
                            this@RestaurantsActivity,
                            "Pressed a restaurant!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun filterRestaurants(restaurants: List<Restaurant>): List<Restaurant> {
        return restaurants
            .filter { it.closingHour < 6 }
            .map { restaurant ->
                val userLat = MockCreator.getUserLatitude()
                val userLong = MockCreator.getUserLongitude()
                val distance = FloatArray(2)

                Location.distanceBetween(
                    userLat,
                    userLong,
                    restaurant.location.latitude,
                    restaurant.location.longitude,
                    distance
                )
                val distanceResult = distance[0] / 1000
                restaurant.distance = distanceResult.toInt()
                return@map restaurant
            }.sortedBy { it.distance }
    }

    private fun parseRestaurants(response: RestaurantListResponse): List<Restaurant> {
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

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

}