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
import kr.co.bepo.foodrecipes.databinding.RecipesRowLayoutBinding
import kr.co.bepo.foodrecipes.models.FoodRecipe
import kr.co.bepo.foodrecipes.models.Result
import kr.co.bepo.foodrecipes.ui.fragment.recipes.RecipesFragmentDirections
import kr.co.bepo.foodrecipes.util.RecipesDiffUtil
import org.jsoup.Jsoup

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    private var recipes: List<Result> = emptyList()

    inner class RecipesViewHolder(
        private val binding: RecipesRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Result) = with(binding) {
            recipeImageView.load(data.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            titleTextView.text = data.title

            val desc = Jsoup.parse(data.summary).text()
            descriptionTextView.text = desc

            heartTextView.text = data.aggregateLikes.toString()
            clockTextView.text = data.readyInMinutes.toString()

            if (data.vegan) {
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
                        RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(data)
                    root.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onRecipeClickListener", e.toString())
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

    fun setData(recipe: FoodRecipe) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, recipe.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = recipe.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}