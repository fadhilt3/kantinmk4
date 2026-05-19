package com.kantinku.app.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.api.ApiClient
import com.kantinku.app.api.ApiService
import com.kantinku.app.model.OrderRequest
import com.kantinku.app.model.OrderResponse
import com.kantinku.app.session.SessionManager
import com.kantinku.app.ui.payment.PaymentActivity
import com.kantinku.app.utils.CartManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {
    private var selected = "qris"
    private var rQris: TextView? = null
    private var rTransfer: TextView? = null
    private var rTunai: TextView? = null
    private var rEwallet: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        (findViewById<View>(R.id.btn_back) as ImageButton).setOnClickListener { finish() }
        rQris = findViewById(R.id.radio_qris)
        rTransfer = findViewById(R.id.radio_transfer)
        rTunai = findViewById(R.id.radio_tunai)
        rEwallet = findViewById(R.id.radio_ewallet)

        // Summary
        val sb = StringBuilder()
        for (ci in CartManager.instance.getItems()) sb.append(ci.food.emoji).append(" ")
            .append(ci.food.name)
            .append(" x").append(ci.qty).append("  ")
            .append(CartManager.rupiah(ci.food.price * ci.qty)).append("\n")
        (findViewById<View>(R.id.tv_order_summary) as TextView).text = sb.toString().trim()
        (findViewById<View>(R.id.tv_total) as TextView).text =
            CartManager.rupiah(CartManager.instance.total)

        selectMethod("qris")
        findViewById<View>(R.id.card_qris).setOnClickListener { selectMethod("qris") }
        findViewById<View>(R.id.card_transfer).setOnClickListener { selectMethod("transfer") }
        findViewById<View>(R.id.card_tunai).setOnClickListener { selectMethod("tunai") }
        findViewById<View>(R.id.card_ewallet).setOnClickListener { selectMethod("ewallet") }

        (findViewById<View>(R.id.btn_pay) as Button).setOnClickListener {
            submitOrder()
        }
    }

    private fun submitOrder() {
        val token = SessionManager.getInstance(this).fetchAuthToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesi habis, silakan login ulang", Toast.LENGTH_SHORT).show()
            return
        }

        val orderItems = CartManager.instance.toOrderItems()
        if (orderItems.isEmpty()) {
            Toast.makeText(this, "Keranjang kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val btnPay = findViewById<Button>(R.id.btn_pay)
        btnPay.isEnabled = false
        btnPay.text = "Memproses..."

        val apiService = ApiClient.client.create(ApiService::class.java)
        val request = OrderRequest(items = orderItems)

        apiService.createOrder("Bearer $token", request).enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                btnPay.isEnabled = true
                btnPay.text = "Bayar Sekarang"

                if (response.isSuccessful) {
                    val orderId = response.body()?.data?.id
                    Toast.makeText(this@CheckoutActivity, "Order berhasil dibuat!", Toast.LENGTH_SHORT).show()

                    val i = Intent(this@CheckoutActivity, PaymentActivity::class.java)
                    i.putExtra("method", selected)
                    i.putExtra("order_id", orderId)
                    startActivity(i)
                } else {
                    Toast.makeText(this@CheckoutActivity, "Gagal membuat order: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                btnPay.isEnabled = true
                btnPay.text = "Bayar Sekarang"
                Toast.makeText(this@CheckoutActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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