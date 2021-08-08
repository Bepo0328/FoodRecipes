package kr.co.bepo.foodrecipes.data

import kotlinx.coroutines.flow.Flow
import kr.co.bepo.foodrecipes.data.database.RecipesDao
import kr.co.bepo.foodrecipes.data.database.entities.FavoritesEntity
import kr.co.bepo.foodrecipes.data.database.entities.RecipesEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) {
        recipesDao.insertFavoriteRecipes(favoritesEntity)
    }

    suspend fun deleteFavoriteRecipes(favoritesEntity: FavoritesEntity) {
        recipesDao.deleteFavoriteRecipes(favoritesEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }
}