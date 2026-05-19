package com.kantinku.app.model

data class Menu(
    val id: Int = 0,
    val nama_menu: String = "",
    val harga: Int = 0,
    val stok: Int = 0,
    val kategori: String = ""
)