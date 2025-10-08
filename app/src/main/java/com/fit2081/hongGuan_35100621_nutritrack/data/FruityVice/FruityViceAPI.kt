package com.fit2081.hongGuan_35100621_nutritrack.data.FruityVice

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface for defining FruityVice API endpoints.
 * Base URL: https://www.fruityvice.com
 */
interface FruityViceAPI {

    /**
     * Endpoint to fetch a specific fruit's nutritional information.
     *
     * @param fruitName The name of the fruit
     * @return Response<FruitModel> containing fruit's nutrition details
     */
    @GET("/api/fruit/{name}")
    suspend fun getFruitByName(@Path("name") fruitName: String): Response<FruityViceResponse>

    companion object {
        private const val BASE_URL = "https://www.fruityvice.com"
        /**
         * Factory method to create an instance of FruityViceAPI using Retrofit.
         */
        fun create(): FruityViceAPI {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(FruityViceAPI::class.java)
        }
    }
}
