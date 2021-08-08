package kr.co.bepo.foodrecipes.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.co.bepo.foodrecipes.R
import kr.co.bepo.foodrecipes.data.database.entities.FavoritesEntity
import kr.co.bepo.foodrecipes.databinding.RecipesRowLayoutBinding
import kr.co.bepo.foodrecipes.ui.fragment.favorites.FavoriteRecipesFragmentDirections
import kr.co.bepo.foodrecipes.util.RecipesDiffUtil
import org.jsoup.Jsoup

class FavoriteRecipesAdapter :
    RecyclerView.Adapter<FavoriteRecipesAdapter.FavoriteRecipesViewHolder>() {

    private var favoriteRecipes = emptyList<FavoritesEntity>()

    inner class FavoriteRecipesViewHolder(
        private val binding: RecipesRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FavoritesEntity) = with(binding) {
            recipeImageView.load(data.result.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            titleTextView.text = data.result.title

            val desc = Jsoup.parse(data.result.summary).text()
            descriptionTextView.text = desc

            heartTextView.text = data.result.aggregateLikes.toString()
            clockTextView.text = data.result.readyInMinutes.toString()

            if (data.result.vegan) {
                leafImageView.setColorFilter(
                    ContextCompat.getColor(
                        leafImageView.context,
                        R.color.green
                    )
                )
                leafTextView.setTextColor(
                    ContextCompat.getColor(
                        leafTextView.context,
                        R.color.green
                    )
                )
            }

            root.setOnClickListener {
                try {
                    val action =
                        FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                            data.result
                        )
                    root.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onRecipeClickListener", e.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipesViewHolder {
        return FavoriteRecipesViewHolder(
            RecipesRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteRecipesViewHolder, position: Int) {
        holder.bind(favoriteRecipes[position])
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun setData(favoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtil = RecipesDiffUtil(this.favoriteRecipes, favoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        this.favoriteRecipes = favoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }
}