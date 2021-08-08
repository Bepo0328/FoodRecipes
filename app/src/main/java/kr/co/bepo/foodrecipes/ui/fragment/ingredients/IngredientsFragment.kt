package kr.co.bepo.foodrecipes.ui.fragment.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.bepo.foodrecipes.adapter.IngredientsAdapter
import kr.co.bepo.foodrecipes.databinding.FragmentIngredientsBinding
import kr.co.bepo.foodrecipes.models.Result
import kr.co.bepo.foodrecipes.util.Constants

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val adapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentIngredientsBinding.inflate(inflater, container, false)
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
        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        myBundle?.extendedIngredients?.let { adapter.setData(it) }
    }

    private fun setupRecyclerView() = with(binding) {
        ingredientsRecyclerView.adapter = adapter
        ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}