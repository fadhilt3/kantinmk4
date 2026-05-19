package com.kantinku.app.ui.payment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.api.ApiClient
import com.kantinku.app.api.ApiService
import com.kantinku.app.model.PaymentRequest
import com.kantinku.app.model.PaymentResponse
import com.kantinku.app.session.SessionManager
import com.kantinku.app.utils.CartManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random

class PaymentActivity : AppCompatActivity() {
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        var method = intent.getStringExtra("method")
        val orderId = intent.getIntExtra("order_id", -1)
        if (method == null) method = "qris"

        (findViewById<View>(R.id.btn_back) as ImageButton).setOnClickListener { finish() }
        (findViewById<View>(R.id.tv_total) as TextView).text =
            CartManager.rupiah(CartManager.instance.total)

        val panelQris = findViewById<View>(R.id.panel_qris)
        val panelTransfer = findViewById<View>(R.id.panel_transfer)
        val panelTunai = findViewById<View>(R.id.panel_tunai)
        val panelEwallet = findViewById<View>(R.id.panel_ewallet)

        panelQris.visibility = View.GONE
        panelTransfer.visibility = View.GONE
        panelTunai.visibility = View.GONE
        panelEwallet.visibility = View.GONE

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        when (method) {
            "transfer" -> {
                tvTitle.text = "Transfer Bank"
                panelTransfer.visibility = View.VISIBLE
                findViewById<View>(R.id.btn_copy).setOnClickListener {
                    val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(ClipData.newPlainText("rekening", "1234567890123"))
                    Toast.makeText(this, "Nomor rekening disalin!", Toast.LENGTH_SHORT).show()
                }
            }
            "tunai" -> {
                tvTitle.text = "Bayar di Tempat"
                panelTunai.visibility = View.VISIBLE
                val code = "#KTN-" + (Random().nextInt(9000) + 1000)
                (findViewById<View>(R.id.tv_order_code) as TextView).text = code
            }
            "ewallet" -> {
                tvTitle.text = "E-Wallet"
                panelEwallet.visibility = View.VISIBLE
                val wallets = arrayOf("GoPay", "OVO", "DANA", "ShopeePay")
                val btnIds = intArrayOf(R.id.btn_gopay, R.id.btn_ovo, R.id.btn_dana, R.id.btn_shopeepay)
                for (i in btnIds.indices) {
                    val wallet = wallets[i]
                    findViewById<View>(btnIds[i]).setOnClickListener {
                        Toast.makeText(this, "Membuka $wallet...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> {
                tvTitle.text = "Bayar QRIS"
                panelQris.visibility = View.VISIBLE
                val tvTimer = findViewById<TextView>(R.id.tv_timer)
                timer = object : CountDownTimer(300000, 1000) {
                    override fun onTick(ms: Long) {
                        tvTimer.text = String.format("%02d:%02d", ms / 60000, (ms % 60000) / 1000)
                    }
                    override fun onFinish() {
                        tvTimer.text = "Kedaluwarsa"
                    }
                }.start()
            }
        }

        val finalMethod = method
        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        btnConfirm.setOnClickListener {
            if (orderId == -1) {
                // Tidak ada order_id, langsung ke sukses (fallback)
                goToSuccess(finalMethod)
                return@setOnClickListener
            }
            submitPayment(orderId, finalMethod, btnConfirm)
        }
    }

    private fun submitPayment(orderId: Int, method: String, btnConfirm: Button) {
        val token = SessionManager.getInstance(this).fetchAuthToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesi habis, silakan login ulang", Toast.LENGTH_SHORT).show()
            return
        }

        btnConfirm.isEnabled = false
        btnConfirm.text = "Memproses..."

        val apiService = ApiClient.client.create(ApiService::class.java)
        val request = PaymentRequest(order_id = orderId)

        apiService.createPayment("Bearer $token", request).enqueue(object : Callback<PaymentResponse> {
            override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                btnConfirm.isEnabled = true
                btnConfirm.text = "Konfirmasi Pembayaran"

                if (response.isSuccessful) {
                    goToSuccess(method)
                } else {
                    Toast.makeText(this@PaymentActivity, "Gagal konfirmasi: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                btnConfirm.isEnabled = true
                btnConfirm.text = "Konfirmasi Pembayaran"
                Toast.makeText(this@PaymentActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToSuccess(method: String) {
        val label = when (method) {
            "transfer" -> "Transfer Bank"
            "tunai" -> "Tunai"
            "ewallet" -> "E-Wallet"
            else -> "QRIS"
        }
        CartManager.instance.clearCart()
        val i = Intent(this, PaymentSuccessActivity::class.java)
        i.putExtra("method", label)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}