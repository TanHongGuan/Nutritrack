import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fit2081.hongGuan_35100621_nutritrack.data.FruityVice.FruityViceRepository
import com.fit2081.hongGuan_35100621_nutritrack.data.FruityVice.FruityViceResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class FruityViceViewModel(
    private val repository: FruityViceRepository = FruityViceRepository()
) : ViewModel() {

    private val _fruitData = MutableStateFlow<FruityViceResponse?>(null)
    val fruitData = _fruitData.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun fetchFruitData(fruitName: String) {
        viewModelScope.launch {
            try {
                val result = repository.getFruitByName(fruitName)
                if (result != null && result.nutritions != null) {
                    _fruitData.value = result
                    _error.value = null // clear any past errors
                } else {
                    _fruitData.value = null
                    _error.value = "Fruit not found."
                }
            } catch (e: IOException) {
                _fruitData.value = null
                _error.value = "No internet connection."
            }
        }
    }

    class FruityViceViewModelFactory(
        private val repository: FruityViceRepository = FruityViceRepository()
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FruityViceViewModel(repository) as T
        }
    }
}
