package com.kantinku.app.ui.history

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        findViewById<View>(R.id.btn_back).setOnClickListener { v: View? -> finish() }
        findViewById<View>(R.id.btn_reorder1).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Pesanan diulang! 🎉",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (findViewById<View?>(R.id.btn_reorder2) != null) findViewById<View>(R.id.btn_reorder2).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Pesanan diulang! 🎉",
                Toast.LENGTH_SHORT
            ).show()
        }
        findViewById<View>(R.id.btn_track).setOnClickListener { v: View? ->
            Toast.makeText(
                this,
                "Fitur lacak pesanan segera hadir! 📍",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
