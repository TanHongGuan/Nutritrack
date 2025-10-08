package com.fit2081.hongGuan_35100621_nutritrack.data.patient

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PatientViewModel(context: Context) : ViewModel() {

    private val patientRepo = PatientRepository(context)

    fun insert(patient: Patient) = viewModelScope.launch {
        patientRepo.insert(patient)
    }

    fun update(patient: Patient) = viewModelScope.launch {
        patientRepo.update(patient)
    }

    fun delete(patient: Patient) = viewModelScope.launch {
        patientRepo.delete(patient)
    }

    fun deleteAllPatients() = viewModelScope.launch {
        patientRepo.deleteAllPatients()
    }

    fun deleteByUserID(userId: String) = viewModelScope.launch {
        patientRepo.deleteByUserID(userId)
    }

    fun updateNameAndPassword(userId: String, name: String, password: String) = viewModelScope.launch {
        patientRepo.updateNameAndPassword(userId, name, password)
    }

    suspend fun getUserPassword(userID: String): String? {
        return patientRepo.getPasswordByUserID(userID)
    }

    suspend fun getUserName(userId: String): String? {
        return patientRepo.getUserName(userId)
    }

    suspend fun getPhoneNumber(userId: String): String? {
        return patientRepo.getPhoneNumber(userId)
    }

    suspend fun getFruitServe(userId: String): String? {
        return patientRepo.getPatientFruitServe(userId)
    }

    suspend fun getFoodVariety(userId: String): String? {
        return patientRepo.getPatientFoodVariety(userId)
    }

    suspend fun getAverageHEIFAMale(): Float {
        return patientRepo.getAverageHEIFAMale()
    }

    suspend fun getAverageHEIFAFemale(): Float {
        return patientRepo.getAverageHEIFAFemale()
    }

    class PatientViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PatientViewModel(appContext) as T
        }
    }
}
