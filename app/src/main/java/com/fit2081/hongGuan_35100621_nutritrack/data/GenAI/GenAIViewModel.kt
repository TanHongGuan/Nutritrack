package com.fit2081.hongGuan_35100621_nutritrack.data.GenAI

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.HongGuan35100621.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GenAIViewModel : ViewModel() {

    /**
     * Mutable state flow to hold the current UI state
     * Initially set to 'UiState.Initial'
     */
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)

    /**
     * Mutable state flow to hold the generated Ai Tips
     */
    private val _aiTips = MutableStateFlow<List<String>>(emptyList())
    val aiTips: StateFlow<List<String>> = _aiTips.asStateFlow()

    /**
     * Publicly exposed immutable state flow for observing the UI state
     */
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    /**
     * Instance of the GenerativeModel used to generate content.
     * The model is initialized with a specific model name and API key
     */
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GENAI_API_KEY
    )

    /**
     * Sends a prompt to the generative AI model and updates the UI state
     * based on the response.
     * Stores the tip if it's new.
     *
     * @param prompt The input text prompt to be sent to the generative model
     */
    fun sendPrompt(prompt: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                val outputContent = response.text
                if (!outputContent.isNullOrEmpty()) {
                    _uiState.value = UiState.Success(outputContent)
                    if (!_aiTips.value.contains(outputContent)) {
                        _aiTips.value = _aiTips.value + outputContent
                    }
                } else {
                    _uiState.value = UiState.Error("Empty response")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No Internet Connection")
            }
        }
    }

}