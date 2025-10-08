package com.fit2081.hongGuan_35100621_nutritrack.data.NutriCoachTips

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fit2081.hongGuan_35100621_nutritrack.data.NutriTrackDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class NutriCoachTipsViewModel(
    private val repository: NutriCoachTipsRepository,
    private val userId: String) : ViewModel() {

    // Get all tips for the user
    fun getTipsForUser(): Flow<List<NutriCoachTip>> {
        return repository.getTipsForUser(userId)
    }

    // Insert a new tip if it's not repeated
    fun insertNewTip(tip: String) {
        viewModelScope.launch {
            // Check if the tip is already in the database
            if (!repository.isRepeatedTip(userId, tip)) {
                val nutriCoachTip = NutriCoachTip(tip = tip, ownerUserId = userId)
                repository.insertTip(nutriCoachTip)  // Insert the tip into the database
            }
        }
    }

    // Clear all tips for the user
    fun deleteAllTipsForUser() {
        viewModelScope.launch {
            repository.deleteAllTipsForUser(userId)
        }
    }

    class NutriCoachTipsViewModelFactory(
        private val context: Context,
        private val userId: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repository = NutriCoachTipsRepository(
                NutriTrackDatabase.getDatabase(context).nutriCoachTipsDao()
            )
            return NutriCoachTipsViewModel(repository, userId) as T
        }
    }
}
