package kr.co.bepo.foodrecipes.ui.fragment.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.co.bepo.foodrecipes.adapter.RecipesAdapter
import kr.co.bepo.foodrecipes.data.database.RecipesEntity
import kr.co.bepo.foodrecipes.databinding.FragmentRecipesBinding
import kr.co.bepo.foodrecipes.models.FoodRecipe
import kr.co.bepo.foodrecipes.util.NetworkResult
import kr.co.bepo.foodrecipes.util.observeOnce
import kr.co.bepo.foodrecipes.util.toInvisible
import kr.co.bepo.foodrecipes.util.toVisible
import kr.co.bepo.foodrecipes.viewmodels.MainViewModel
import kr.co.bepo.foodrecipes.viewmodels.RecipesViewModel

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(
            MainViewModel::class.java
        )
    }
    private val recipesViewModel: RecipesViewModel by lazy {
        ViewModelProvider(requireActivity()).get(
            RecipesViewModel::class.java
        )
    }
    private val adapter: RecipesAdapter by lazy { RecipesAdapter() }

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRecipesBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        readDatabase()
    }

    private fun setupRecyclerView() = with(binding) {
        shimmerRecyclerView.adapter = adapter
        shimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    adapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { adapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

            errorViewVisibility(
                response,
                mainViewModel.readRecipes.value
            )
        }
    }

    private fun errorViewVisibility(
        apiResponse: NetworkResult<FoodRecipe>?,
        database: List<RecipesEntity>?
    ) = with(binding) {
        if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
            errorImageView.toVisible()
            errorTextView.toVisible()
            errorTextView.text = apiResponse.message.toString()
        } else if (apiResponse is NetworkResult.Loading) {
            errorImageView.toInvisible()
            errorTextView.toInvisible()
        } else if (apiResponse is NetworkResult.Success) {
            errorImageView.toInvisible()
            errorTextView.toInvisible()
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    adapter.setData(database[0].foodRecipe)
                }
            }
        }
    }

    private fun showShimmerEffect() = with(binding) {
        shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() = with(binding) {
        shimmerRecyclerView.hideShimmer()
    }
}