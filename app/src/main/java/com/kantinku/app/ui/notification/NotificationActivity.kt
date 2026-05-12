package com.kantinku.app.ui.notification

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.adapter.NotifAdapter
import com.kantinku.app.utils.DataProvider

class NotificationActivity : AppCompatActivity() {
    private var adapter: NotifAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        findViewById<View>(R.id.btn_back).setOnClickListener { v: View? -> finish() }

        val rv = findViewById<RecyclerView>(R.id.rv_notif)
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter = NotifAdapter(DataProvider.getNotifications())
        rv.adapter = adapter

        (findViewById<View>(R.id.tv_read_all) as TextView).setOnClickListener { v: View? -> adapter!!.markAllRead() }
    }
}
