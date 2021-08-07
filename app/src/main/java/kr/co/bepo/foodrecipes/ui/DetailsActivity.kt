package kr.co.bepo.foodrecipes.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.bepo.foodrecipes.R
import kr.co.bepo.foodrecipes.adapter.PagerAdapter
import kr.co.bepo.foodrecipes.databinding.ActivityDetailsBinding
import kr.co.bepo.foodrecipes.ui.fragment.ingredients.IngredientsFragment
import kr.co.bepo.foodrecipes.ui.fragment.instructions.InstructionsFragment
import kr.co.bepo.foodrecipes.ui.fragment.overview.OverviewFragment
import kr.co.bepo.foodrecipes.util.Constants.Companion.RECIPE_RESULT_KEY

class DetailsActivity : AppCompatActivity() {

    private val binding: ActivityDetailsBinding by lazy {
        ActivityDetailsBinding.inflate(
            layoutInflater
        )
    }

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initViewPager()
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this@DetailsActivity, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewPager() = with(binding) {
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            supportFragmentManager,
            lifecycle
        )

        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}