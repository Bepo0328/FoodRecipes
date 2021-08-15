package kr.co.bepo.foodrecipes.ui.fragment.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import kr.co.bepo.foodrecipes.R
import kr.co.bepo.foodrecipes.databinding.FragmentOverviewBinding
import kr.co.bepo.foodrecipes.models.Result
import kr.co.bepo.foodrecipes.util.Constants.Companion.RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentOverviewBinding.inflate(inflater, container, false)
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
        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result

        mainImageView.load(myBundle.image)
        titleTextView.text = myBundle.title
        likesTextView.text = myBundle.aggregateLikes.toString()
        timeTextView.text = myBundle.readyInMinutes.toString()

        myBundle.summary.let {
            val summary = Jsoup.parse(it).text()
            summaryTextView.text = summary
        }

        updateColors(myBundle.vegetarian, vegetarianImageView, vegetarianTextView)
        updateColors(myBundle.vegan, veganImageView, veganTextView)
        updateColors(myBundle.glutenFree, glutenFreeImageView, glutenFreeTextView)
        updateColors(myBundle.dairyFree, dairyFreeImageView, dairyFreeTextView)
        updateColors(myBundle.veryHealthy, healthyImageView, healthTextView)
        updateColors(myBundle.cheap, cheapImageView, cheapTextView)
    }

    private fun updateColors(stateIsOn: Boolean, iv: ImageView, tv: TextView) {
        if (stateIsOn) {
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            iv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }
}