package com.kantinku.app.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kantinku.app.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        findViewById<TextView>(R.id.btn_back).setOnClickListener { finish() }

        val developers = listOf(
            Pair("Fadhil Rekh Saputra", "Android Developer"),
            Pair("Rizky Madyachandra Ramadhan", "Android Developer"),
            Pair("Saskia Syifa Salsabila", "UI/UX Designer"),
            Pair("Tanisha Nadia Hanz", "UI/UX Designer")
        )

        val container = findViewById<LinearLayout>(R.id.developer_container)

        developers.forEachIndexed { index, (name, role) ->
            val item = LayoutInflater.from(this)
                .inflate(R.layout.item_developer, container, false)

            val initials = name.split(" ")
                .take(2)
                .joinToString("") { it[0].toString() }
                .uppercase()

            item.findViewById<TextView>(R.id.tv_initials).text = initials
            item.findViewById<TextView>(R.id.tv_dev_name).text = name
            item.findViewById<TextView>(R.id.tv_dev_role).text = role

            container.addView(item)

            if (index < developers.size - 1) {
                val divider = View(this)
                divider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1
                ).apply { marginStart = 72 }
                divider.setBackgroundResource(R.color.divider)
                container.addView(divider)
            }
        }
    }
}