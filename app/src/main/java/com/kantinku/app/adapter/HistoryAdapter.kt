package com.kantinku.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.model.OrderData
import com.kantinku.app.model.getEmojiByKategori
import com.kantinku.app.utils.CartManager

class HistoryAdapter(private var orders: List<OrderData>) :
    RecyclerView.Adapter<HistoryAdapter.VH>() {

    fun updateData(newOrders: List<OrderData>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val order = orders[pos]

        h.tvOrderId.text = "#KTN-${order.id}"
        h.tvOrderDate.text = order.created_at?.take(10) ?: "-"

        when (order.status) {
            "paid" -> {
                h.tvStatus.text = "Selesai"
                h.tvStatus.setTextColor(h.itemView.context.getColor(R.color.success))
                h.tvStatus.setBackgroundResource(R.drawable.bg_tag_green)
            }
            "pending" -> {
                h.tvStatus.text = "⏳ Diproses"
                h.tvStatus.setTextColor(h.itemView.context.getColor(R.color.warning))
                h.tvStatus.setBackgroundResource(R.drawable.bg_tag_yellow)
            }
            else -> {
                h.tvStatus.text = order.status
            }
        }

        val itemsText = order.items
            .filter { it.menu != null }
            .joinToString("  •  ") { item ->
                val emoji = getEmojiByKategori(item.menu.kategori)
                "$emoji ${item.menu.nama_menu} x${item.jumlah}"
            }
        h.tvItems.text = if (itemsText.isEmpty()) "Detail tidak tersedia" else itemsText
        h.tvTotal.text = CartManager.rupiah(order.total_harga.toInt())
    }

    override fun getItemCount(): Int = orders.size

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvOrderId: TextView = v.findViewById(R.id.tv_order_id)
        val tvOrderDate: TextView = v.findViewById(R.id.tv_order_date)
        val tvStatus: TextView = v.findViewById(R.id.tv_status)
        val tvItems: TextView = v.findViewById(R.id.tv_items)
        val tvTotal: TextView = v.findViewById(R.id.tv_total)
    }
}