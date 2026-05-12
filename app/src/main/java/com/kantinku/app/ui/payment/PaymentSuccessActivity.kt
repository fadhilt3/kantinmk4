package com.kantinku.app.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.ui.history.HistoryActivity
import com.kantinku.app.ui.home.MainActivity
import com.kantinku.app.utils.CartManager
import java.util.Random

class PaymentSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        val method = intent.getStringExtra("method")
        (findViewById<View>(R.id.tv_order_no) as TextView).text =
            "#KTN-" + (Random().nextInt(9000) + 1000)
        (findViewById<View>(R.id.tv_method) as TextView).text =
            method ?: "QRIS"

        val sb = StringBuilder()
        for (ci in CartManager.instance.getItems()) sb.append(ci.food.emoji).append(" ")
            .append(ci.food.name)
            .append(" x").append(ci.qty).append("\n")
        (findViewById<View>(R.id.tv_items) as TextView).text =
            sb.toString().trim { it <= ' ' }
        (findViewById<View>(R.id.tv_total) as TextView).text =
            CartManager.rupiah(CartManager.instance.total)

        (findViewById<View>(R.id.btn_history) as Button).setOnClickListener { v: View? ->
            CartManager.instance.clearCart()
            startActivity(Intent(this, HistoryActivity::class.java))
            finish()
        }
        (findViewById<View>(R.id.btn_home) as Button).setOnClickListener { v: View? ->
            CartManager.instance.clearCart()
            val i = Intent(this, MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }
    }
}