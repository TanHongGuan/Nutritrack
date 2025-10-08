package com.fit2081.hongGuan_35100621_nutritrack.data.FruityVice

/**
 * Repository that manages communication with the FruityVice API.
 */
class FruityViceRepository(private val api: FruityViceAPI = FruityViceAPI.create()) {

    /**
     * Fetch nutritional information for target fruit.
     *
     * @param fruitName The name of the fruit
     * @return Response<FruityViceResponse> from the API.
     */
    suspend fun getFruitByName(fruitName: String): FruityViceResponse? {
        val response = api.getFruitByName(fruitName)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
