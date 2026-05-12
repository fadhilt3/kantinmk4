package com.kantinku.app.ui.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kantinku.app.R
import com.kantinku.app.ui.cart.CartFragment
import com.kantinku.app.ui.menu.MenuFragment
import com.kantinku.app.ui.profile.ProfileFragment
import com.kantinku.app.utils.CartManager

class MainActivity : AppCompatActivity() {
    private var lblHome: TextView? = null
    private var lblMenu: TextView? = null
    private var lblCart: TextView? = null
    private var lblProfile: TextView? = null
    private var cartBadge: TextView? = null
    private var currentTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lblHome = findViewById(R.id.lbl_home)
        lblMenu = findViewById(R.id.lbl_menu)
        lblCart = findViewById(R.id.lbl_cart)
        lblProfile = findViewById(R.id.lbl_profile)
        cartBadge = findViewById(R.id.cart_badge)

        CartManager.getInstance().setListener { this.updateBadge() }

        goTab(HomeFragment(), 0)
        findViewById<View>(R.id.tab_home).setOnClickListener { v: View? ->
            goTab(
                HomeFragment(),
                0
            )
        }
        findViewById<View>(R.id.tab_menu).setOnClickListener { v: View? ->
            goTab(
                MenuFragment(),
                1
            )
        }
        findViewById<View>(R.id.tab_cart).setOnClickListener { v: View? ->
            goTab(
                CartFragment(),
                2
            )
        }
        findViewById<View>(R.id.tab_profile).setOnClickListener { v: View? ->
            goTab(
                ProfileFragment(),
                3
            )
        }
    }

    private fun goTab(f: Fragment, idx: Int) {
        currentTab = idx
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, f).commit()
        val on = getColor(R.color.primary)
        val off = getColor(R.color.text_hint)
        lblHome!!.setTextColor(if (idx == 0) on else off)
        lblMenu!!.setTextColor(if (idx == 1) on else off)
        lblCart!!.setTextColor(if (idx == 2) on else off)
        lblProfile!!.setTextColor(if (idx == 3) on else off)
    }

    fun updateBadge() {
        val n = CartManager.getInstance().totalQty
        cartBadge!!.text = n.toString()
        cartBadge!!.visibility = if (n > 0) View.VISIBLE else View.GONE
    }

    fun goCart() {
        goTab(CartFragment(), 2)
    }

    fun goMenu() {
        goTab(MenuFragment(), 1)
    }

    override fun onResume() {
        super.onResume()
        updateBadge()
    }
}
