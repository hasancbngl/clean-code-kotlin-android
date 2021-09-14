package com.betterandroid.restaurantscorner.features.restaurants

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.betterandroid.restaurantscorner.R
import com.betterandroid.restaurantscorner.domain.models.restaurants.Restaurant
import com.betterandroid.restaurantscorner.data.restaurants.RestaurantParser.parseRestaurants
import com.betterandroid.restaurantscorner.bussiness.restaurants.RestaurantRules.filterRestaurants
import com.betterandroid.restaurantscorner.api.restaurants.RestaurantsRestClient
import com.betterandroid.restaurantscorner.features.restaurants.RestaurantsViewModel.getDisplayRestaurants
import kotlinx.android.synthetic.main.activity_restaurants.*
import java.util.*

class RestaurantsActivity : AppCompatActivity() {

    private lateinit var restaurantsAdapter: RestaurantsAdapter
    private val restaurantClient = RestaurantsRestClient()

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
        restaurantClient.getRestaurants { response ->
            //parsing, filtering, displaying
            val parsedRestaurants = parseRestaurants(response)
            val filteredRestaurants = filterRestaurants(parsedRestaurants)
            displayRestaurants(filteredRestaurants)
        }
    }

    private fun displayRestaurants(restaurants: List<Restaurant>) {
        //find the restaurant where the distance is 50
        val foundRestaurant = restaurants.find { it.distance == 50 }

        restaurantsAdapter.restaurants = getDisplayRestaurants(restaurants)
        restaurantsAdapter.clickListener =
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

    override fun onDestroy() {
        super.onDestroy()
        restaurantClient.stopStream()
    }

}