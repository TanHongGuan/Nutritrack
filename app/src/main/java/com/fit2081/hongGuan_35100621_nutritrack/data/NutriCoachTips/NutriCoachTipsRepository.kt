package com.fit2081.hongGuan_35100621_nutritrack.data.NutriCoachTips

import kotlinx.coroutines.flow.Flow

class NutriCoachTipsRepository(private val tipsDao: NutriCoachTipsDao) {

    fun getTipsForUser(userId: String): Flow<List<NutriCoachTip>> {
        return tipsDao.getTipsByUserID(userId)
    }

    suspend fun insertTip(tip: NutriCoachTip) {
        tipsDao.insertTip(tip)
    }

    suspend fun deleteAllTipsForUser(userId: String) {
        tipsDao.deleteTipsByUserID(userId)
    }

    suspend fun isRepeatedTip(userId: String, tip: String): Boolean {
        return tipsDao.isRepeatedTip(userId, tip)
    }

    suspend fun clearAllTips() {
        tipsDao.clearAllTips()
    }
}
