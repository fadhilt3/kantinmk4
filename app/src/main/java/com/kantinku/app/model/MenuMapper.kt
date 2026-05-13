package com.kantinku.app.model

fun Menu.toFoodItem(): FoodItem {
    return FoodItem(
        id = this.id.toString(),
        name = this.nama_menu ?: "",
        emoji = getEmojiByKategori(this.kategori),
        kantin = "Kantin",
        description = this.nama_menu ?: "",
        price = this.harga,
        rating = 0f,
        reviewCount = 0,
        category = this.kategori?.lowercase() ?: "semua",
        isPopular = false,
        isNew = false
    )
}

fun getEmojiByKategori(kategori: String?): String {
    return when (kategori?.lowercase()) {
        "nasi" -> "🍛"
        "mie" -> "🍜"
        "kuah" -> "🥘"
        "lauk" -> "🍗"
        "sayur" -> "🥗"
        "minuman" -> "🧋"
        "snack" -> "🍌"
        else -> "🍽️"
    }
}