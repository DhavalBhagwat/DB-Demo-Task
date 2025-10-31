package com.example.holdings.ui.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.holdings.R
import com.example.holdings.databinding.ActivityMainBinding
import com.example.holdings.ui.fragments.HoldingsFragment

class MainActivity : AppCompatActivity() {

    companion object {
        /** Titles for the tabs in the tab layout */
        private val TAB_TITLES = listOf("POSITIONS", "HOLDINGS")
    }

    /** Instance of [ActivityMainBinding] */
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, statusBarHeight, 0, 0)
            insets
        }
        setupTabs()
        binding.bottomNav.selectedItemId = R.id.nav_portfolio
        loadFragment(HoldingsFragment())
    }

    /**
     * Function to setup tab layout with titles.
     */
    private fun setupTabs() {
        TAB_TITLES.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
    }

    /**
     * Function to load the specified fragment into the fragment container.
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}
