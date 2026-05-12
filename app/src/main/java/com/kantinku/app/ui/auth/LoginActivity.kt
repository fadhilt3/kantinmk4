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
import java.util.Locale

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
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
        tvForgot.setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Fitur reset password segera hadir!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun doLogin() {
        val email = etEmail!!.text.toString().trim { it <= ' ' }
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

        // Simulasi login - ambil nama dari email
        var name = email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        name = name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
        getInstance(this).saveLogin(name, email, name.lowercase(Locale.getDefault()))

        Toast.makeText(this, "Selamat datang, $name! 👋", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
