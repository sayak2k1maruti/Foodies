package com.internshala.foodies.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.internshala.foodies.model.Restaurants

@Entity(tableName = "restaurants")
data class RestaurantsEntity(
    @PrimaryKey val restaurant_id:Int,
    @ColumnInfo(name = "restaurant_name") val restaurantName:String,
    @ColumnInfo(name = "restaurant_rating") val restaurantRating:String,
    @ColumnInfo(name = "cost_for_one") val restaurantCostOfOne:String,
    @ColumnInfo(name = "image_url") val restaurantImageUrl:String
)