package com.example.holdings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.holdings.R
import com.example.holdings.databinding.ItemHoldingBinding
import com.example.holdings.domain.model.Holding

/**
 * Adapter to show list of holdings in [RecyclerView].
 */
class HoldingsAdapter : RecyclerView.Adapter<HoldingsAdapter.HoldingViewHolder>() {

    private val items = mutableListOf<Holding>()

    fun setItems(newList: List<Holding>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class HoldingViewHolder(private val binding: ItemHoldingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Holding) = with(binding) {
            // Symbol
            textSymbol.text = item.symbol

            // Quantity
            textQty.text = item.quantity.toString()

            // LTP
            textLtp.text = "₹%.2f".format(item.ltp)

            // P&L
            val pnl = item.pnl
            textPnL.text = "₹%.2f".format(pnl)

            // Apply color
            val colorRes = if (pnl >= 0) R.color.colorProfit else R.color.colorLoss
            textPnL.setTextColor(ContextCompat.getColor(root.context, colorRes))
        }
    }
}
