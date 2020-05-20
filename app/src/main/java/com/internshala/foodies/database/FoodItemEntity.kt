package com.internshala.foodies.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FoodItems")
data class FoodItemEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cost") val cost: String,
    @ColumnInfo(name = "restaurant_id") val restaurantId: String
)