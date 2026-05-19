package com.kantinku.app.model

data class PaymentResponse(
    val message: String,
    val data: PaymentData
)

data class PaymentData(
    val id: Int,
    val order_id: Int,
    val jumlah_bayar: Double,
    val status: String
)