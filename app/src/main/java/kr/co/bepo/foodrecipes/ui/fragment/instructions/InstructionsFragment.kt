package kr.co.bepo.foodrecipes.ui.fragment.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import kr.co.bepo.foodrecipes.databinding.FragmentInstructionsBinding
import kr.co.bepo.foodrecipes.models.Result
import kr.co.bepo.foodrecipes.util.Constants
import kr.co.bepo.foodrecipes.util.toGone
import kr.co.bepo.foodrecipes.util.toVisible

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentInstructionsBinding.inflate(inflater, container, false)
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
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        instructionsWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress >= 100) {
                    progressBar.toGone()
                } else {
                    progressBar.toVisible()
                }
            }
        }

        val websiteUrl: String = myBundle!!.sourceUrl
        instructionsWebView.loadUrl(websiteUrl)
    }
}