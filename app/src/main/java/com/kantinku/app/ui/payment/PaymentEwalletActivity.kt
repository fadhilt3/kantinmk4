package com.kantinku.app.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.utils.CartManager

class PaymentEwalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_ewallet)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        val tvTotal = findViewById<TextView>(R.id.tv_total)

        btnBack.setOnClickListener { v: View? -> finish() }
        tvTotal.text = CartManager.rupiah(CartManager.getInstance().total)

        findViewById<View>(R.id.btn_gopay).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Mengalihkan ke GoPay... 🟢",
                Toast.LENGTH_SHORT
            ).show()
        }
        findViewById<View>(R.id.btn_ovo).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Mengalihkan ke OVO... 🟣",
                Toast.LENGTH_SHORT
            ).show()
        }
        findViewById<View>(R.id.btn_dana).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Mengalihkan ke DANA... 🔵",
                Toast.LENGTH_SHORT
            ).show()
        }
        findViewById<View>(R.id.btn_shopeepay).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Mengalihkan ke ShopeePay... 🟠",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnConfirm.setOnClickListener { v: View? ->
            val intent = Intent(
                this,
                PaymentSuccessActivity::class.java
            )
            intent.putExtra("payment_method", "E-Wallet")
            startActivity(intent)
            finish()
        }
    }
}