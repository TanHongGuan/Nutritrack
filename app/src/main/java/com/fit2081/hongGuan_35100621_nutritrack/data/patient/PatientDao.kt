package com.fit2081.hongGuan_35100621_nutritrack.data.patient

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    // Inserts a new patient into database
    @Insert
    suspend fun insert(patient: Patient)

    // Updates an existing patient in database
    @Update
    suspend fun update(patient: Patient)

    // Deletes a specific patient from database
    @Delete
    suspend fun delete(patient: Patient)

    // Deletes all patients from database.
    @Query("DELETE FROM Patient")
    suspend fun deleteAllPatients()

    // Delete specific patient from database
    @Query("DELETE FROM Patient where userID = :id")
    suspend fun deleteByUserID(id : String)

    // Retrieve all patients from database
    @Query("SELECT * FROM Patient ORDER BY userID ASC")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT * FROM Patient")
    suspend fun getAllPatientsOnce(): List<Patient>

    @Query("SELECT COUNT(*) FROM Patient")
    suspend fun getPatientCount(): Int

    @Query("SELECT name FROM Patient where userID = :id")
    suspend fun getUserName(id: String): String

    @Query("SELECT phoneNumber FROM Patient where userID = :id")
    suspend fun getPhoneNumber(id: String): String

    // Update patient name and password
    @Query("UPDATE Patient SET name = :name WHERE userID = :id")
    suspend fun updateName(id: String, name: String)

    // Update patient name and password
    @Query("UPDATE Patient SET password = :password WHERE userID = :id")
    suspend fun updatePassword(id: String, password: String)

    // Retrieve patient password
    @Query("SELECT password FROM Patient WHERE userID = :id LIMIT 1")
    suspend fun getPasswordByUserID(id: String): String?

    // Retrieve patient FruitServe
    @Query("SELECT FruitServe FROM Patient WHERE userID = :id LIMIT 1")
    suspend fun getUserFruitServe(id: String): String?

    // Retrieve patient FruitServe
    @Query("SELECT FruitVariety FROM Patient WHERE userID = :id LIMIT 1")
    suspend fun getUserFruitVariety(id: String): String?

    @Query("SELECT AVG(CAST(HEIFAtotalscore AS FLOAT)) FROM Patient WHERE sex = 'Male'")
    suspend fun getAverageHEIFAMale(): Float?

    @Query("SELECT AVG(CAST(HEIFAtotalscore AS FLOAT)) FROM Patient WHERE sex = 'Female'")
    suspend fun getAverageHEIFAFemale(): Float?


}