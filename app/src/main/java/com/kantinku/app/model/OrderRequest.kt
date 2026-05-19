package com.kantinku.app.model

data class OrderRequest(
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val menu_id: Int,
    val jumlah: Int
)