package com.kantinku.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.utils.CartManager
import com.kantinku.app.utils.CartManager.CartItem

class CartAdapter(private var items: List<CartItem>, private val onChange: Runnable) :
    RecyclerView.Adapter<CartAdapter.VH>() {
    fun refresh(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false))
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val ci = items[pos]
        h.emoji.text = ci.food.emoji
        h.name.text = ci.food.name
        h.kantin.text = ci.food.kantin
        h.price.text = CartManager.rupiah(ci.food.price * ci.qty)
        h.qty.text = ci.qty.toString()
        h.minus.setOnClickListener { v: View? ->
            CartManager.getInstance().removeOne(ci.food.id)
            onChange.run()
        }
        h.plus.setOnClickListener { v: View? ->
            CartManager.getInstance().addItem(ci.food)
            onChange.run()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        var emoji: TextView = v.findViewById(R.id.tv_emoji)
        var name: TextView = v.findViewById(R.id.tv_name)
        var kantin: TextView = v.findViewById(R.id.tv_kantin)
        var price: TextView = v.findViewById(R.id.tv_price)
        var qty: TextView = v.findViewById(R.id.tv_qty)
        var minus: TextView = v.findViewById(R.id.btn_minus)
        var plus: TextView = v.findViewById(R.id.btn_plus)
    }
}
