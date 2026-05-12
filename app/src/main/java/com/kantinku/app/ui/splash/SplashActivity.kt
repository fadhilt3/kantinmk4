package com.kantinku.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R
import com.kantinku.app.session.SessionManager.Companion.getInstance
import com.kantinku.app.ui.auth.LoginActivity
import com.kantinku.app.ui.home.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val sm =
                getInstance(this)
            val intent = if (sm.isLoggedIn)
                Intent(this, MainActivity::class.java)
            else
                Intent(this, LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }, 1800)
    }
}
