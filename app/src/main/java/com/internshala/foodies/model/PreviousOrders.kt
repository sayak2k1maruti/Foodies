package com.internshala.foodies.model

import com.internshala.foodies.database.FoodItemEntity

data class PreviousOrders (
    val id:String,
    val restaurantName:String,
    val totalCost:String,
    val timeOfOrder:String,
    val foodItems:List<FoodItemEntity>
)