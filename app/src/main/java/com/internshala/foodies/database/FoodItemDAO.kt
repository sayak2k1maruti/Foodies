package com.internshala.foodies.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodItemDAO {
    @Insert
    fun insertFoodItem(foodItemEntity: FoodItemEntity)

    @Delete
    fun deleteFoodItem(foodItemEntity: FoodItemEntity)

    @Query("SELECT * FROM FoodItems")
    fun gerAllFoodItems():List<FoodItemEntity>

    @Query("SELECT * FROM FoodItems WHERE id = :id")
    fun getFoodItemById(id:String):FoodItemEntity

    @Query("DELETE  FROM FoodItems")
    fun deleteAll()

}