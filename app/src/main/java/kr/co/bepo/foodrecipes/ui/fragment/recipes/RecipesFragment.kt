package kr.co.bepo.foodrecipes.ui.fragment.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kr.co.bepo.foodrecipes.adapter.RecipesAdapter
import kr.co.bepo.foodrecipes.databinding.FragmentRecipesBinding
import kr.co.bepo.foodrecipes.util.NetworkResult
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
        requestApiData()
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
        }
    }


    private fun setupRecyclerView() = with(binding) {
        shimmerRecyclerView.adapter = adapter
        shimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() = with(binding) {
        shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() = with(binding) {
        shimmerRecyclerView.hideShimmer()
    }
}