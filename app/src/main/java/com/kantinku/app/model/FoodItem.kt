package com.kantinku.app.model

class FoodItem(
    @JvmField val id: String, @JvmField val name: String, @JvmField val emoji: String, val kantin: String,
    val description: String, @JvmField val price: Int, @JvmField val rating: Float, val reviewCount: Int,
    val category: String, @JvmField val isPopular: Boolean, val isNew: Boolean
) {
    val isAvailable: Boolean = true
}
