package kr.co.bepo.foodrecipes.util

import kr.co.bepo.foodrecipes.BuildConfig

class Constants {

    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = BuildConfig.SPOONACULAR_API_KEY

        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_APIKEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // ROOM Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
    }
}