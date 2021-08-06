package kr.co.bepo.foodrecipes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kr.co.bepo.foodrecipes.util.Constants.Companion.API_KEY
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_APIKEY
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_DIET
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_NUMBER
import kr.co.bepo.foodrecipes.util.Constants.Companion.QUERY_TYPE

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_APIKEY] = API_KEY
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

}