package com.internshala.foodies.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantsDAO {
    @Insert
    fun insertRestaurant(restaurantsEntity: RestaurantsEntity)

    @Delete
    fun deleteRestaurant(restaurantsEntity: RestaurantsEntity)

    @Query("SELECT * FROM restaurants")
    fun gerAllRestaurants():List<RestaurantsEntity>

    @Query("SELECT * FROM restaurants WHERE restaurant_id = :id")
    fun getRestaurantById(id:String):RestaurantsEntity
}