package com.kantinku.app.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.adapter.CartAdapter
import com.kantinku.app.ui.checkout.CheckoutActivity
import com.kantinku.app.ui.home.MainActivity
import com.kantinku.app.utils.CartManager

class CartFragment : Fragment() {
    private var adapter: CartAdapter? = null
    private var tvSubtotal: TextView? = null
    private var tvTotal: TextView? = null
    private var emptyState: View? = null
    private var rv: RecyclerView? = null

    override fun onCreateView(inf: LayoutInflater, c: ViewGroup?, s: Bundle?): View? {
        return inf.inflate(R.layout.fragment_cart, c, false)
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)

        rv = v.findViewById(R.id.rv_cart)
        emptyState = v.findViewById(R.id.empty_state)
        tvSubtotal = v.findViewById(R.id.tv_subtotal)
        tvTotal = v.findViewById(R.id.tv_total)
        val btnCheckout = v.findViewById<Button>(R.id.btn_checkout)
        val tvClear = v.findViewById<TextView>(R.id.tv_clear)

        rv?.layoutManager = LinearLayoutManager(requireContext())
        rv?.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        adapter = CartAdapter(CartManager.instance.getItems()) { refresh() }
        rv?.adapter = adapter

        tvClear.setOnClickListener {
            CartManager.instance.clearCart()
            refresh()
        }

        btnCheckout.setOnClickListener {
            if (!CartManager.instance.isEmpty) {
                startActivity(Intent(requireContext(), CheckoutActivity::class.java))
            }
        }

        refresh()
    }

    private fun refresh() {
        adapter?.refresh(CartManager.instance.getItems())

        val empty = CartManager.instance.isEmpty
        rv?.visibility = if (empty) View.GONE else View.VISIBLE
        emptyState?.visibility = if (empty) View.VISIBLE else View.GONE

        tvSubtotal?.text = CartManager.rupiah(CartManager.instance.subtotal)
        tvTotal?.text = CartManager.rupiah(CartManager.instance.total)

        (activity as? MainActivity)?.updateBadge()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }
}