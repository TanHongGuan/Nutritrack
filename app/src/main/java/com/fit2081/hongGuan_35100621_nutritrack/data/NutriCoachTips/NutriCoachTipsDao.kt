package com.fit2081.hongGuan_35100621_nutritrack.data.NutriCoachTips

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NutriCoachTipsDao {


    @Insert
    suspend fun insertTip(tip: NutriCoachTip)

    @Query("SELECT * FROM NutriCoachTips WHERE ownerUserId = :userId")
    fun getTipsByUserID(userId: String): Flow<List<NutriCoachTip>>

    @Query("SELECT EXISTS(SELECT 1 FROM NutriCoachTips WHERE ownerUserId = :userId AND tip = :tip)")
    suspend fun isRepeatedTip(userId: String, tip: String): Boolean

    @Query("DELETE FROM NutriCoachTips WHERE ownerUserId = :userId")
    suspend fun deleteTipsByUserID(userId: String)

    @Query("DELETE FROM NutriCoachTips")
    suspend fun clearAllTips()
}
