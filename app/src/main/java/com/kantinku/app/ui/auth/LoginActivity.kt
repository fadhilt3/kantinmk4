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
import com.kantinku.app.api.ApiClient
import com.kantinku.app.api.ApiService
import com.kantinku.app.model.LoginRequest
import com.kantinku.app.model.LoginResponse
import com.kantinku.app.session.SessionManager.Companion.getInstance
import com.kantinku.app.ui.home.MainActivity

class LoginActivity : AppCompatActivity() {
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val tvRegister = findViewById<TextView>(R.id.tv_register)
        val tvForgot = findViewById<TextView>(R.id.tv_forgot)

        btnLogin.setOnClickListener { v: View? -> doLogin() }
        tvRegister.setOnClickListener { v: View? ->
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        tvForgot.setOnClickListener { v: View? ->
            Toast.makeText(this, "Fitur reset password segera hadir!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doLogin() {
        val email = etEmail!!.text.toString().trim()
        val password = etPassword!!.text.toString()

        if (TextUtils.isEmpty(email)) {
            etEmail!!.error = "Email wajib diisi"
            return
        }
        if (TextUtils.isEmpty(password)) {
            etPassword!!.error = "Password wajib diisi"
            return
        }
        if (password.length < 6) {
            etPassword!!.error = "Password minimal 6 karakter"
            return
        }

        val apiService = ApiClient.client.create(ApiService::class.java)
        val request = LoginRequest(email, password)

        apiService.login(request).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()!!
                    val session = getInstance(this@LoginActivity)
                    session.saveLogin(
                        body.user.name,
                        body.user.email,
                        body.user.name
                    )
                    // Simpan token dengan benar
                    session.saveToken(body.access_token)

                    Toast.makeText(
                        this@LoginActivity,
                        "Selamat datang, ${body.user.name}! 👋",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Email atau password salah!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Gagal koneksi: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}