package kr.co.bepo.foodrecipes.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import kr.co.bepo.foodrecipes.R
import kr.co.bepo.foodrecipes.data.database.entities.FavoritesEntity
import kr.co.bepo.foodrecipes.databinding.RecipesRowLayoutBinding
import kr.co.bepo.foodrecipes.ui.fragment.favorites.FavoriteRecipesFragmentDirections
import kr.co.bepo.foodrecipes.util.RecipesDiffUtil
import kr.co.bepo.foodrecipes.viewmodels.MainViewModel
import org.jsoup.Jsoup

class FavoriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteRecipesAdapter.FavoriteRecipesViewHolder>(), ActionMode.Callback {

    private var multiSelection = false

    private lateinit var actionMode: ActionMode
    private lateinit var rootView: View

    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var favoriteRecipesViewHolders = arrayListOf<FavoriteRecipesViewHolder>()
    private var favoriteRecipes = emptyList<FavoritesEntity>()

    inner class FavoriteRecipesViewHolder(
        private val binding: RecipesRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FavoritesEntity) = with(binding) {
            rootView = binding.root

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

            /**
             * Single Click Listener
             */
            root.setOnClickListener {
                if (multiSelection) {
                    applySelection(data)
                } else {
                    val action =
                        FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                            data.result
                        )
                    root.findNavController().navigate(action)
                }
            }

            /**
             * Long Click Listener
             */
            recipesRowLayout.setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    requireActivity.startActionMode(this@FavoriteRecipesAdapter)
                }
                applySelection(data)
                true
            }
        }

        private fun applySelection(currentRecipe: FavoritesEntity) {
            if (selectedRecipes.contains(currentRecipe)) {
                selectedRecipes.remove(currentRecipe)
                changeRecipeStyle(R.color.cardBackgroundColor, R.color.strokeColor)
                applyActionModeTitle()
            } else {
                selectedRecipes.add(currentRecipe)
                changeRecipeStyle(R.color.cardBackgroundLightColor, R.color.primaryColor)
                applyActionModeTitle()
            }
        }

        fun changeRecipeStyle(backgroundColor: Int, strokeColor: Int) = with(binding) {
            recipesRowLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity,
                    backgroundColor
                )
            )
            rowCardView.strokeColor = ContextCompat.getColor(requireActivity, strokeColor)
        }

        private fun applyActionModeTitle() {
            when (selectedRecipes.size) {
                0 -> {
                    actionMode.finish()
                    multiSelection = false
                }
                1 -> {
                    actionMode.title = "${selectedRecipes.size} item selected"
                }
                else -> {
                    actionMode.title = "${selectedRecipes.size} items selected"
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteRecipesAdapter.FavoriteRecipesViewHolder {
        return FavoriteRecipesViewHolder(
            RecipesRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteRecipesViewHolder, position: Int) {
        favoriteRecipesViewHolders.add(holder)
        holder.bind(favoriteRecipes[position])
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        this.actionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menuItem: MenuItem?): Boolean {
        if (menuItem?.itemId == R.id.delete_favorite_recipe_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipes(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed.")

            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        favoriteRecipesViewHolders.forEach { holder ->
            holder.changeRecipeStyle(R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    fun setData(favoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtil = RecipesDiffUtil(this.favoriteRecipes, favoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        this.favoriteRecipes = favoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    fun clearContextualActionMode() {
        if (this::actionMode.isInitialized) {
            actionMode.finish()
        }
    }
}