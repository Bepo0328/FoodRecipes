package kr.co.bepo.foodrecipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.co.bepo.foodrecipes.R
import kr.co.bepo.foodrecipes.databinding.IngredientsRowLayoutBinding
import kr.co.bepo.foodrecipes.models.ExtendedIngredient
import kr.co.bepo.foodrecipes.util.Constants.Companion.BASE_IMAGE_URL
import kr.co.bepo.foodrecipes.util.RecipesDiffUtil

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    private var ingredients = emptyList<ExtendedIngredient>()

    inner class IngredientViewHolder(
        private val binding: IngredientsRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ExtendedIngredient) = with(binding) {
            ingredientImageView.load(BASE_IMAGE_URL + data.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            ingredientNameTextView.text = data.name.uppercase()
            ingredientAmountTextView.text = data.amount.toString()
            ingredientUnitTextView.text = data.unit
            ingredientConsistencyTextView.text = data.consistency
            ingredientOriginalTextView.text = data.original
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(
            IngredientsRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setData(ingredients: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffUtil(this.ingredients, ingredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        this.ingredients = ingredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}