package com.fit2081.hongGuan_35100621_nutritrack.data.FoodIntake

import android.content.Context
import com.fit2081.hongGuan_35100621_nutritrack.data.NutriTrackDatabase
import kotlinx.coroutines.flow.Flow

class FoodIntakeRepository{
    // Property to hold the FoodIntakeDao instance
    var foodIntakeDao : FoodIntakeDao

    // Constructor to initialize the FoodIntakeDao
    constructor(context: Context) {
        // Get the FoodIntakeDao instance from the NutriTrackDatabase
        foodIntakeDao = NutriTrackDatabase.getDatabase(context).foodIntakeDao()
    }

    // Function to insert a FoodIntake into database
    suspend fun insert(foodIntake: FoodIntake) {
        // Call the insert function from the FoodIntakeDao
        foodIntakeDao.insert(foodIntake)
    }

    // Function to delete a foodIntake from database
    suspend fun delete(foodIntake: FoodIntake) {
        // Call the delete function from FoodIntakeDao
        foodIntakeDao.delete(foodIntake)
    }

    // Function to update a foodIntake from database
    suspend fun update(foodIntake: FoodIntake) {
        // Call the update function from FoodIntakeDao
        foodIntakeDao.update(foodIntake)
    }

    // Function to delete all foodIntake from database
    suspend fun deleteAllFoodIntake() {
        // Call the delete function from FoodIntakeDao
        foodIntakeDao.deleteAllFoodIntake()
    }

    // Get a flow list of questionnaire responses
    fun getAllFoodIntake(): Flow<List<FoodIntake>> {
        return foodIntakeDao.getAllFoodIntake()
    }

    // Get the latest questionnaire responses of a user
    suspend fun getLatestFoodIntake(userId: String): FoodIntake? {
        return foodIntakeDao.getFoodIntakeByUserId(userId)
    }
}