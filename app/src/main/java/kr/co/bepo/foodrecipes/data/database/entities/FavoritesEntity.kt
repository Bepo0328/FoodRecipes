package kr.co.bepo.foodrecipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kr.co.bepo.foodrecipes.models.Result
import kr.co.bepo.foodrecipes.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)