package kr.co.bepo.foodrecipes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.co.bepo.foodrecipes.data.DataStoreRepository
import kr.co.bepo.foodrecipes.util.Constants.Companion.API_KEY
import kr.co.bepo.foodrecipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import kr.co.bepo.foodrecipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import kr.co.bepo.foodrecipes.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_APIKEY
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_DIET
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_NUMBER
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_TYPE
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_APIKEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

}