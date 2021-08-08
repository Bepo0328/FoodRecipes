package kr.co.bepo.foodrecipes.ui.fragment.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kr.co.bepo.foodrecipes.R
import kr.co.bepo.foodrecipes.adapter.FavoriteRecipesAdapter
import kr.co.bepo.foodrecipes.databinding.FragmentFavoriteRecipesBinding
import kr.co.bepo.foodrecipes.util.toInvisible
import kr.co.bepo.foodrecipes.util.toVisible
import kr.co.bepo.foodrecipes.viewmodels.MainViewModel

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val adapter: FavoriteRecipesAdapter by lazy {
        FavoriteRecipesAdapter(
            requireActivity(),
            mainViewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.clearContextualActionMode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupRecyclerView()
    }

    private fun initViews() = with(binding) {
        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all_favorite_recipe_menu) {
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar() = with(binding) {
        Snackbar.make(
            root,
            "All recipes removed.",
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }
}