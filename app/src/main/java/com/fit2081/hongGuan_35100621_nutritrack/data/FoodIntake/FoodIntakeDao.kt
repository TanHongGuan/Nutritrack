package com.fit2081.hongGuan_35100621_nutritrack.data.FoodIntake

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodIntakeDao{
    @Insert
    suspend fun insert(foodIntake: FoodIntake)

    @Delete
    suspend fun delete(foodIntake: FoodIntake)

    @Update
    suspend fun update(foodIntake: FoodIntake)

    @Query("DELETE FROM FoodIntake")
    suspend fun deleteAllFoodIntake()

    // Retrieves user foodIntake data
    @Query("SELECT * FROM FoodIntake ORDER BY userID")
    fun getAllFoodIntake(): Flow<List<FoodIntake>>

    // Deletes user foodIntake data from databse
    @Query("DELETE FROM FoodIntake WHERE userID = :id")
    suspend fun deleteById(id: String)

    // Get user's latest FoodIntake
    @Query("SELECT * FROM FoodIntake WHERE userID = :id ORDER BY questionnaireID DESC LIMIT 1")
    suspend fun getFoodIntakeByUserId(id: String): FoodIntake?


}

