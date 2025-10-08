package com.fit2081.hongGuan_35100621_nutritrack.data.patient

import android.content.Context
import com.fit2081.hongGuan_35100621_nutritrack.data.NutriTrackDatabase
import kotlinx.coroutines.flow.Flow

class PatientRepository(context: Context) {

    private val patientDao: PatientDao = NutriTrackDatabase.getDatabase(context).patientDao()

    val allPatients: Flow<List<Patient>> = patientDao.getAllPatients()

    suspend fun insert(patient: Patient) {
        patientDao.insert(patient)
    }

    suspend fun update(patient: Patient) {
        patientDao.update(patient)
    }

    suspend fun delete(patient: Patient) {
        patientDao.delete(patient)
    }

    suspend fun deleteAllPatients() {
        patientDao.deleteAllPatients()
    }

    suspend fun deleteByUserID(userId: String) {
        patientDao.deleteByUserID(userId)
    }

    suspend fun getUserName(userId: String): String? {
        return patientDao.getUserName(userId)
    }


    suspend fun getPhoneNumber(userId: String): String?{
        return patientDao.getPhoneNumber(userId)
    }

    suspend fun updateNameAndPassword(userId: String, name: String, password: String) {
        patientDao.updateName(userId, name)
        patientDao.updatePassword(userId,password)
    }

    suspend fun getPasswordByUserID(id: String): String? {
        return patientDao.getPasswordByUserID(id)
    }

    suspend fun getAllPatientsOnce(): List<Patient> {
        return patientDao.getAllPatientsOnce()
    }

    suspend fun getPatientFruitServe(id: String): String? {
        return patientDao.getUserFruitServe(id)
    }

    suspend fun getPatientFoodVariety(id: String): String? {
        return patientDao.getUserFruitVariety(id)
    }

    suspend fun getAverageHEIFAMale(): Float {
        return patientDao.getAverageHEIFAMale() ?: 0f
    }

    suspend fun getAverageHEIFAFemale(): Float {
        return patientDao.getAverageHEIFAFemale() ?: 0f
    }
}
