package kr.co.bepo.foodrecipes.data

import kotlinx.coroutines.flow.Flow
import kr.co.bepo.foodrecipes.data.database.RecipesDao
import kr.co.bepo.foodrecipes.data.database.RecipesEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }
}