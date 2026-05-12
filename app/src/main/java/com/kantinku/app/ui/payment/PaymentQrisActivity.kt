package com.kantinku.app.ui.payment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.utils.CartManager

class PaymentQrisActivity : AppCompatActivity() {
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_qris)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnPaid = findViewById<Button>(R.id.btn_paid)
        val tvTotal = findViewById<TextView>(R.id.tv_total)
        val tvTimer = findViewById<TextView>(R.id.tv_timer)

        btnBack.setOnClickListener { v: View? -> finish() }
        tvTotal.text = CartManager.rupiah(CartManager.instance.total)

        // 5 minute countdown
        countDownTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(ms: Long) {
                val m = ms / 60000
                val s = (ms % 60000) / 1000
                tvTimer.text = String.format("%02d:%02d", m, s)
            }

            override fun onFinish() {
                tvTimer.text = "00:00"
            }
        }.start()

        btnPaid.setOnClickListener { v: View? ->
            val intent = Intent(
                this,
                PaymentSuccessActivity::class.java
            )
            intent.putExtra("payment_method", "QRIS")
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) countDownTimer!!.cancel()
    }
}