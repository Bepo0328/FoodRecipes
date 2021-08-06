package kr.co.bepo.foodrecipes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.co.bepo.foodrecipes.R
import kr.co.bepo.foodrecipes.databinding.RecipesRowLayoutBinding
import kr.co.bepo.foodrecipes.models.FoodRecipe
import kr.co.bepo.foodrecipes.models.Result
import kr.co.bepo.foodrecipes.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    private var recipes: List<Result> = emptyList()

    inner class RecipesViewHolder(
        private val binding: RecipesRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) = with(binding) {

            recipeImageView.load(result.image) {
                crossfade(600)
            }

            titleTextView.text = result.title
            descriptionTextView.text = result.summary

            heartTextView.text = result.aggregateLikes.toString()
            clockTextView.text = result.readyInMinutes.toString()

            applyVeganColor(leafTextView, result.vegan)
            applyVeganColor(leafImageView, result.vegan)
        }

        private fun applyVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder =
        RecipesViewHolder(
            RecipesRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecipesAdapter.RecipesViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int =
        recipes.size

    fun setData(newData: FoodRecipe) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}