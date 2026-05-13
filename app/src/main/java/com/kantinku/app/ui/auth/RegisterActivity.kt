package com.kantinku.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.session.SessionManager.Companion.getInstance
import com.kantinku.app.ui.home.MainActivity
import com.kantinku.app.api.ApiClient
import com.kantinku.app.api.ApiService
import com.kantinku.app.model.LoginResponse
import com.kantinku.app.model.RegisterRequest

class RegisterActivity : AppCompatActivity() {
    private var etName: EditText? = null
    private var etUsername: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var etConfirm: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.et_name)
        etUsername = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirm = findViewById(R.id.et_confirm)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val tvLogin = findViewById<TextView>(R.id.tv_login)

        btnRegister.setOnClickListener { v: View? -> doRegister() }
        tvLogin.setOnClickListener { v: View? ->
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun doRegister() {
        val name = etName!!.text.toString().trim()
        val email = etEmail!!.text.toString().trim()
        val password = etPassword!!.text.toString()
        val confirm = etConfirm!!.text.toString()

        if (TextUtils.isEmpty(name)) {
            etName!!.error = "Nama wajib diisi"
            return
        }
        if (TextUtils.isEmpty(email)) {
            etEmail!!.error = "Email wajib diisi"
            return
        }
        if (password.length < 6) {
            etPassword!!.error = "Password minimal 6 karakter"
            return
        }
        if (password != confirm) {
            etConfirm!!.error = "Password tidak cocok"
            return
        }

        val apiService = ApiClient.client.create(ApiService::class.java)
        val request = RegisterRequest(name, email, password)

        apiService.register(request).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()!!
                    getInstance(this@RegisterActivity).saveLogin(
                        body.user.name,
                        body.user.email,
                        body.access_token
                    )
                    Toast.makeText(
                        this@RegisterActivity,
                        "Akun berhasil dibuat! Selamat datang, ${body.user.name} 🎉",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Gagal register! Email mungkin sudah dipakai.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Gagal koneksi: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
