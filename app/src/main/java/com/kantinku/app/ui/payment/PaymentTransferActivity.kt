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

class PaymentTransferActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_transfer)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        val tvTotal = findViewById<TextView>(R.id.tv_total)

        btnBack.setOnClickListener { v: View? -> finish() }
        tvTotal.text = CartManager.rupiah(CartManager.getInstance().total)

        findViewById<View>(R.id.btn_copy_bca).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Nomor rekening BCA disalin! 📋",
                Toast.LENGTH_SHORT
            ).show()
        }
        findViewById<View>(R.id.btn_copy_mandiri).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Nomor rekening Mandiri disalin! 📋",
                Toast.LENGTH_SHORT
            ).show()
        }
        findViewById<View>(R.id.btn_copy_bni).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Nomor rekening BNI disalin! 📋",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnConfirm.setOnClickListener { v: View? ->
            val intent = Intent(
                this,
                PaymentSuccessActivity::class.java
            )
            intent.putExtra("payment_method", "Transfer Bank")
            startActivity(intent)
            finish()
        }
    }
}