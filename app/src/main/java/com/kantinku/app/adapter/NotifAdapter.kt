package com.kantinku.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.model.NotificationItem

class NotifAdapter(private val items: List<NotificationItem>) :
    RecyclerView.Adapter<NotifAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        )
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val n = items[pos]
        h.title.text = n.title
        h.message.text = n.message
        h.time.text = n.time
        h.dot.visibility = if (n.isRead) View.INVISIBLE else View.VISIBLE
        when (n.type) {
            "promo" -> h.icon.text = "🔥"
            "order" -> h.icon.text = "🛍️"
            else -> h.icon.text = "ℹ️"
        }
        h.itemView.setOnClickListener { v: View? ->
            n.setRead()
            notifyItemChanged(pos)
        }
    }

    fun markAllRead() {
        for (n in items) n.setRead()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        var title: TextView = v.findViewById(R.id.tv_title)
        var message: TextView = v.findViewById(R.id.tv_message)
        var time: TextView = v.findViewById(R.id.tv_time)
        var icon: TextView = v.findViewById(R.id.tv_icon)
        var dot: View = v.findViewById(R.id.unread_dot)
    }
}
