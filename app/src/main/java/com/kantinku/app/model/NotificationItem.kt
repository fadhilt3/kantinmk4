package com.kantinku.app.model

class NotificationItem(
    @JvmField val title: String, @JvmField val message: String, @JvmField val time: String, // "order", "promo", "info"
    @JvmField val type: String
) {
    var isRead: Boolean = false
        private set

    fun setRead() {
        this.isRead = true
    }
}
