package com.example.holdings.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holdings.R
import com.example.holdings.adapters.HoldingsAdapter
import com.example.holdings.core.Result
import com.example.holdings.databinding.FragmentHoldingsBinding
import com.example.holdings.domain.model.HoldingsSummary
import com.example.holdings.utils.formatCurrency
import com.example.holdings.utils.formatHeaderValue
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HoldingsFragment : Fragment() {

    /** Instance of [FragmentHoldingsBinding] */
    private lateinit var binding: FragmentHoldingsBinding

    /** Instance of [HoldingsViewModel] */
    private val viewModel: HoldingsViewModel by viewModel()

    /** Instance of [HoldingsAdapter] */
    private lateinit var adapter: HoldingsAdapter

    /** Flag to track summary card expansion state */
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoldingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupSummaryCard()
        observeHoldings()
        observeSummary()
    }

    /**
     * Function to setup [RecyclerView].
     */
    private fun setupRecycler() = with(binding) {
        adapter = HoldingsAdapter()
        recyclerHoldings.layoutManager = LinearLayoutManager(requireContext())
        recyclerHoldings.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.swipeRefresh.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorSecondary
        )
    }

    /**
     * Function to observe holdings state from [ViewModel].
     */
    private fun observeHoldings() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.holdingsState.collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        adapter.setItems(result.data)
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is Result.Error -> {
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is Result.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                    }
                    is Result.NoNetwork -> {
                        binding.swipeRefresh.isRefreshing = false
                        Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /**
     * Function to observe holdings summary from [ViewModel].
     */
    private fun observeSummary() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.summary.collectLatest { summary ->
                bindSummary(summary)
            }
        }
    }

    /** Function to bind the holdings summary data to the UI components.
     *
     * @param summary [HoldingsSummary] data to bind.
     */
    private fun bindSummary(summary: HoldingsSummary) = with(binding) {
        // header
        textHeaderValue.text = formatHeaderValue(summary.totalPnl, summary.totalPnlPercent)
        val headerColor = if (summary.totalPnl >= 0) R.color.colorProfit else R.color.colorLoss
        textHeaderValue.setTextColor(requireContext().getColor(headerColor))

        // expanded details
        tvCurrentValue.text = formatCurrency(summary.totalCurrentValue)
        tvInvestValue.text = formatCurrency(summary.totalInvestment)
        tvTodayValue.text = formatCurrency(summary.totalTodaysPnl)

        val totalColor = if (summary.totalTodaysPnl >= 0) R.color.colorProfit else R.color.colorLoss
        tvTodayValue.setTextColor(requireContext().getColor(totalColor))
    }

    private fun setupSummaryCard() = with(binding) {
        // start collapsed
        detailsContainer.visibility = View.GONE
        chevron.rotation = 0f

        summaryCard.setOnClickListener { toggleSummary() }
    }

    /**
     * Expands detailsContainer upward (above header) instead of below.
     * Keeps Profit & Loss header pinned at the bottom.
     */
    private fun toggleSummary() = with(binding) {
        isExpanded = !isExpanded

        if (isExpanded) {
            // EXPAND UPWARDS
            detailsContainer.measure(
                View.MeasureSpec.makeMeasureSpec(summaryCard.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
            val targetHeight = detailsContainer.measuredHeight

            // prepare for expansion
            detailsContainer.layoutParams.height = 0
            detailsContainer.visibility = View.VISIBLE
            detailsContainer.translationY = targetHeight.toFloat()
            detailsContainer.alpha = 0f

            // animate height + upward slide
            val heightAnimator = ValueAnimator.ofInt(0, targetHeight)
            heightAnimator.addUpdateListener { anim ->
                val value = anim.animatedValue as Int
                detailsContainer.layoutParams.height = value
                detailsContainer.requestLayout()
                detailsContainer.translationY = targetHeight - value.toFloat()
                detailsContainer.alpha = value.toFloat() / targetHeight
            }
            heightAnimator.duration = 300
            heightAnimator.interpolator = OvershootInterpolator(0.4f)
            heightAnimator.start()

            // Rotate chevron to point down (expanded)
            chevron.animate()
                .rotation(180f)
                .setInterpolator(OvershootInterpolator(0.6f))
                .setDuration(300)
                .start()

            // elevate card slightly
            summaryCard.animate().translationZ(16f).setDuration(300).start()
        } else {
            // COLLAPSE DOWNWARDS
            val initialHeight = detailsContainer.measuredHeight

            val heightAnimator = ValueAnimator.ofInt(initialHeight, 0)
            heightAnimator.addUpdateListener { anim ->
                val value = anim.animatedValue as Int
                detailsContainer.layoutParams.height = value
                detailsContainer.requestLayout()
                detailsContainer.translationY = initialHeight - value.toFloat()
                detailsContainer.alpha = value.toFloat() / initialHeight
            }
            heightAnimator.duration = 250
            heightAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    detailsContainer.visibility = View.GONE
                }
            })
            heightAnimator.start()

            // Rotate chevron to collapsed state
            chevron.animate()
                .rotation(0f)
                .setInterpolator(OvershootInterpolator(0.6f))
                .setDuration(250)
                .start()

            // Lower elevation
            summaryCard.animate().translationZ(8f).setDuration(250).start()
        }
    }
}
