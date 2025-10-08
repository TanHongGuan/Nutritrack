package com.fit2081.hongGuan_35100621_nutritrack.data.FruityVice

/**
 * Data model that represents the response from the FruityVice API for target fruit.
 */
data class FruityViceResponse(
    val name: String,
    val family: String,
    val genus: String,
    val order: String,
    val nutritions: Nutrition
)

data class Nutrition(
    val carbohydrates: Double,
    val protein: Double,
    val fat: Double,
    val calories: Int,
    val sugar: Double
)
