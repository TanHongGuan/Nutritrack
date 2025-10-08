package com.fit2081.hongGuan_35100621_nutritrack.data.FoodIntake

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FoodIntakeViewModel(context: Context) : ViewModel() {

    // Create the repository with context
    private val foodIntakeRepository = FoodIntakeRepository(context)

    // Get all food intake records as a Flow
    val allFoodIntakes: Flow<List<FoodIntake>> = foodIntakeRepository.getAllFoodIntake()

    // Insert a new food intake
    fun insert(foodIntake: FoodIntake) = viewModelScope.launch {
        foodIntakeRepository.insert(foodIntake)
    }

    // Delete a specific food intake
    fun delete(foodIntake: FoodIntake) = viewModelScope.launch {
        foodIntakeRepository.delete(foodIntake)
    }

    // Update an existing food intake
    fun update(foodIntake: FoodIntake) = viewModelScope.launch {
        foodIntakeRepository.update(foodIntake)
    }

    // Get the latest questionnaire responses of a user
    suspend fun getLatestFoodIntake(userId: String): FoodIntake? {
        return foodIntakeRepository.getLatestFoodIntake(userId)
    }


    // a view model factory that sets the context for the viewmodel
    // The ViewModelProvider.Factory inferface is used to create view models/
    class FoodIntakeViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FoodIntakeViewModel(appContext) as T
        }
    }

}
