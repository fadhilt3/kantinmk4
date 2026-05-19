package com.kantinku.app.model

data class OrderResponse(
    val message: String,
    val data: OrderData
)

data class OrderData(
    val id: Int,
    val user_id: Int,
    val status: String,
    val total_harga: Double,
    val created_at: String?,
    val items: List<OrderItemData>
)

data class OrderItemData(
    val id: Int,
    val menu_id: Int,
    val jumlah: Int,
    val harga: Double,
    val menu: Menu
)