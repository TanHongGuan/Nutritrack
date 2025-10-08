package com.fit2081.hongGuan_35100621_nutritrack.data.NutriCoachTips

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "NutriCoachTips",
    foreignKeys = [ForeignKey(
        entity = com.fit2081.hongGuan_35100621_nutritrack.data.patient.Patient::class,
        parentColumns = ["userID"],
        childColumns = ["ownerUserId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class NutriCoachTip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tip: String,

    val ownerUserId: String
)
