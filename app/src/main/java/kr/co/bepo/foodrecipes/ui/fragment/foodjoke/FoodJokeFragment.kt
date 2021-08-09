package kr.co.bepo.foodrecipes.ui.fragment.foodjoke

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.co.bepo.foodrecipes.R
import kr.co.bepo.foodrecipes.databinding.FragmentFoodJokeBinding
import kr.co.bepo.foodrecipes.util.Constants.Companion.API_KEY
import kr.co.bepo.foodrecipes.util.NetworkResult
import kr.co.bepo.foodrecipes.util.toInvisible
import kr.co.bepo.foodrecipes.util.toVisible
import kr.co.bepo.foodrecipes.viewmodels.MainViewModel

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private var _binding: FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private var foodJoke = "No Food Joke"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFoodJokeBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        setHasOptionsMenu(true)

        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Error -> {
                    progressBar.toInvisible()
                    foodJokeCardView.toVisible()
                    loadDataFromCacheAndVisibility(response.message)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    progressBar.toVisible()
                    foodJokeCardView.toInvisible()
                    Log.d("FoodJokeFragment", "Loading")
                }
                is NetworkResult.Success -> {
                    progressBar.toInvisible()
                    foodJokeCardView.toVisible()
                    foodJokeTextView.text = response.data?.text

                    foodJokeErrorImageView.toInvisible()
                    foodJokeErrorTextView.toInvisible()

                    if (response.data != null) {
                        foodJoke = response.data.text
                    }
                }
            }
        }
    }

    private fun loadDataFromCacheAndVisibility(message: String?) = with(binding) {
        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner) { database ->
                if (database.isNullOrEmpty()) {
                    foodJokeCardView.toInvisible()

                    foodJokeErrorImageView.toVisible()
                    foodJokeErrorTextView.toVisible()
                    foodJokeErrorTextView.text = message.toString()
                } else {
                    foodJokeTextView.text = database[0].foodJoke.text
                    foodJoke = database[0].foodJoke.text
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_joke_menu) {
            val sharedIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                this.type = "text/plain"
            }
            startActivity(sharedIntent)
        }
        return super.onOptionsItemSelected(item)
    }

}