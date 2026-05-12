package com.kantinku.app.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.utils.CartManager

class PaymentTunaiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_tunai)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        val tvTotal = findViewById<TextView>(R.id.tv_total)

        btnBack.setOnClickListener { v: View? -> finish() }
        tvTotal.text = CartManager.rupiah(CartManager.instance.total)

        btnConfirm.setOnClickListener { v: View? ->
            val intent = Intent(
                this,
                PaymentSuccessActivity::class.java
            )
            intent.putExtra("payment_method", "Bayar di Tempat")
            startActivity(intent)
            finish()
        }
    }
}