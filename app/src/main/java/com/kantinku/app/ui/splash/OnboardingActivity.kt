package com.kantinku.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.ui.auth.LoginActivity
import com.kantinku.app.ui.auth.RegisterActivity

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val btnRegister = findViewById<Button>(R.id.btn_register)
        val btnLogin = findViewById<Button>(R.id.btn_login)

        btnRegister.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        btnLogin.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }
    }
}
