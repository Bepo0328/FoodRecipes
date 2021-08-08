package kr.co.bepo.foodrecipes.ui.fragment.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kr.co.bepo.foodrecipes.adapter.FavoriteRecipesAdapter
import kr.co.bepo.foodrecipes.databinding.FragmentFavoriteRecipesBinding
import kr.co.bepo.foodrecipes.util.toInvisible
import kr.co.bepo.foodrecipes.util.toVisible
import kr.co.bepo.foodrecipes.viewmodels.MainViewModel

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    private val adapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter() }
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupRecyclerView()
    }

    private fun initViews() = with(binding) {
        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) { favoritesEntity ->
            if (favoritesEntity.isNullOrEmpty()) {
                noDataImageView.toVisible()
                noDataTextView.toVisible()
                favoriteRecipesRecyclerView.toInvisible()
            } else {
                noDataImageView.toInvisible()
                noDataTextView.toInvisible()
                favoriteRecipesRecyclerView.toVisible()
                adapter.setData(favoritesEntity)
            }
        }
    }

    private fun setupRecyclerView() = with(binding) {
        favoriteRecipesRecyclerView.adapter = adapter
        favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}