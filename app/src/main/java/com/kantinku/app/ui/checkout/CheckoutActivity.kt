package com.kantinku.app.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.ui.payment.PaymentActivity
import com.kantinku.app.utils.CartManager

class CheckoutActivity : AppCompatActivity() {
    private var selected = "qris"
    private var rQris: TextView? = null
    private var rTransfer: TextView? = null
    private var rTunai: TextView? = null
    private var rEwallet: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        (findViewById<View>(R.id.btn_back) as ImageButton).setOnClickListener { v: View? -> finish() }
        rQris = findViewById(R.id.radio_qris)
        rTransfer = findViewById(R.id.radio_transfer)
        rTunai = findViewById(R.id.radio_tunai)
        rEwallet = findViewById(R.id.radio_ewallet)

        // Summary
        val sb = StringBuilder()
        for (ci in CartManager.getInstance().items) sb.append(ci.food.emoji).append(" ")
            .append(ci.food.name)
            .append(" x").append(ci.qty).append("  ")
            .append(CartManager.rupiah(ci.food.price * ci.qty)).append("\n")
        (findViewById<View>(R.id.tv_order_summary) as TextView).text =
            sb.toString().trim { it <= ' ' }
        (findViewById<View>(R.id.tv_total) as TextView).text =
            CartManager.rupiah(CartManager.getInstance().total)

        selectMethod("qris")
        findViewById<View>(R.id.card_qris).setOnClickListener { v: View? ->
            selectMethod(
                "qris"
            )
        }
        findViewById<View>(R.id.card_transfer).setOnClickListener { v: View? ->
            selectMethod(
                "transfer"
            )
        }
        findViewById<View>(R.id.card_tunai).setOnClickListener { v: View? ->
            selectMethod(
                "tunai"
            )
        }
        findViewById<View>(R.id.card_ewallet).setOnClickListener { v: View? ->
            selectMethod(
                "ewallet"
            )
        }

        (findViewById<View>(R.id.btn_pay) as Button).setOnClickListener { v: View? ->
            val i = Intent(this, PaymentActivity::class.java)
            i.putExtra("method", selected)
            startActivity(i)
        }
    }

    private fun selectMethod(m: String) {
        selected = m
        val on = getColor(R.color.primary)
        val off = getColor(R.color.text_hint)
        rQris!!.text = if (m == "qris") "●" else "○"
        rQris!!.setTextColor(if (m == "qris") on else off)
        rTransfer!!.text = if (m == "transfer") "●" else "○"
        rTransfer!!.setTextColor(if (m == "transfer") on else off)
        rTunai!!.text = if (m == "tunai") "●" else "○"
        rTunai!!.setTextColor(if (m == "tunai") on else off)
        rEwallet!!.text = if (m == "ewallet") "●" else "○"
        rEwallet!!.setTextColor(if (m == "ewallet") on else off)
    }
}
