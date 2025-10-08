package com.fit2081.hongGuan_35100621_nutritrack.data

import com.fit2081.hongGuan_35100621_nutritrack.data.FoodIntake.FoodIntake
import com.fit2081.hongGuan_35100621_nutritrack.data.patient.Patient
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fit2081.hongGuan_35100621_nutritrack.data.FoodIntake.FoodIntakeDao
import com.fit2081.hongGuan_35100621_nutritrack.data.NutriCoachTips.NutriCoachTip
import com.fit2081.hongGuan_35100621_nutritrack.data.NutriCoachTips.NutriCoachTipsDao
import com.fit2081.hongGuan_35100621_nutritrack.data.patient.PatientDao
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * This is the database class for the application. It is a Room database.
 * It contains three entities : [ com.fit2081.hongGuan_35100621_nutritrack.data.patient.Patient ], [ com.fit2081.hongGuan_35100621_nutritrack.data.FoodIntake.FoodIntake ], [ com.fit2081.hongGuan_35100621_nutritrack.data.NutriCoachTips.NutriCoachTips ]
 * The version is 5 and exportSchema is false
 */
@Database(entities = [Patient::class, FoodIntake::class, NutriCoachTip::class], version = 4, exportSchema = false)

abstract class NutriTrackDatabase: RoomDatabase() {

    /**
     * Returns the [ PatientDao ] object.
     * This is an abstract function that is implemented by Room
     */

    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun nutriCoachTipsDao(): NutriCoachTipsDao


    companion object{
        /**
         * This is a volatile variable that holds the database instance
         * It is volatile so that it is immediately visible to all threads
         */
        @Volatile
        private var Instance:NutriTrackDatabase? = null

        /**
         * Returns the database instance
         * If the instance is null, it creates a new database instance
         * @param context The context of the application
         */
        fun getDatabase(context: Context): NutriTrackDatabase {
            // If instance is not null, return it, otherwise create a new database instance
            // synchronized means that only one thread that can access this code at a time
            // NutriTrackDatabase is the name of the database
            return Instance?: synchronized(this) {
                Room.databaseBuilder(context, NutriTrackDatabase::class.java, "NutriTrackDatabase")
                    .fallbackToDestructiveMigration()// wipe database if versions doesn't match
                    .build().also {Instance = it}
            }
        }
    }

    /**
     * Returns the [ FoodIntakeDao ] object.
     * This is an abstract function that is implemented by Room
     */
}

/**
 * This is the function to load the CSV file data into this room database on first launch
 */
suspend fun loadCSVFirstLaunch(context: Context, database: NutriTrackDatabase) {

    try {
        val inputStream = context.assets.open("user_data.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val headers = reader.readLine().split(",")

        reader.useLines { lines ->
            lines.forEachIndexed { index, line ->

                val values = line.split(",")
                val phoneNumber = values[0]
                val userID = values[1]
                val userSex = values[2]

                fun getValue(headerName: String): String {
                    val targetHeader = if ("HEIFA" in headerName) {
                        "$headerName$userSex"
                    } else {
                        headerName
                    }

                    val headerIndex = headers.indexOf(targetHeader)
                    val value = if (headerIndex in values.indices) values[headerIndex] else ""


                    return value
                }

                val patient = Patient(
                    userID = userID,
                    phoneNumber = phoneNumber,
                    sex = userSex,
                    HEIFAtotalscore = getValue("HEIFAtotalscore"),
                    DiscretionaryHEIFAscore = getValue("DiscretionaryHEIFAscore"),
                    VegetablesHEIFAscore = getValue("VegetablesHEIFAscore"),
                    FruitHEIFAscore = getValue("FruitHEIFAscore"),
                    GrainsandcerealsHEIFAscore = getValue("GrainsandcerealsHEIFAscore"),
                    WholegrainsHEIFAscore = getValue("WholegrainsHEIFAscore"),
                    MeatandalternativesHEIFAscore = getValue("MeatandalternativesHEIFAscore"),
                    DairyandalternativesHEIFAscore = getValue("DairyandalternativesHEIFAscore"),
                    SodiumHEIFAscore = getValue("SodiumHEIFAscore"),
                    AlcoholHEIFAscore = getValue("AlcoholHEIFAscore"),
                    WaterHEIFAscore = getValue("WaterHEIFAscore"),
                    SugarHEIFAscore = getValue("SugarHEIFAscore"),
                    SaturatedFatHEIFAscore = getValue("SaturatedFatHEIFAscore"),
                    UnsaturatedFatHEIFAscore = getValue("UnsaturatedFatHEIFAscore"),
                    FruitServe = getValue("Fruitservesize"),
                    FruitVariety = getValue("Fruitvariationsscore"),
                    name = null,
                    password = null
                )

                database.patientDao().insert(patient)
            }
        }

    } catch (e: Exception) {
        Toast.makeText(context, "CSV load failed: ${e.message}", Toast.LENGTH_LONG).show()
    }
}



