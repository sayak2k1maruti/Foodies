package com.internshala.foodies.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantsEntity::class],version = 1)
abstract class RestaurantsDatabase:RoomDatabase() {
    abstract fun restaurantDAO():RestaurantsDAO
}