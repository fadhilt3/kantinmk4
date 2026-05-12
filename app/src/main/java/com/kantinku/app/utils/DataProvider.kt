package com.kantinku.app.utils

import com.kantinku.app.model.FoodItem
import com.kantinku.app.model.NotificationItem

object DataProvider {
    val allMenu: List<FoodItem>
        get() {
            val list: MutableList<FoodItem> = ArrayList()
            list.add(
                FoodItem(
                    "1",
                    "Nasi Goreng Spesial",
                    "🍳",
                    "Kantin Utama",
                    "Nasi goreng dengan telur, ayam suwir, dan kerupuk",
                    13000,
                    4.8f,
                    128,
                    "nasi",
                    true,
                    false
                )
            )
            list.add(
                FoodItem(
                    "2",
                    "Nasi Padang Lengkap",
                    "🍛",
                    "Kantin Utama",
                    "Rendang, gulai, sayur nangka, dan nasi putih",
                    15000,
                    4.9f,
                    256,
                    "nasi",
                    true,
                    false
                )
            )
            list.add(
                FoodItem(
                    "3",
                    "Mie Ayam Bakso",
                    "🍜",
                    "Kantin 2",
                    "Mie ayam komplit dengan bakso dan pangsit goreng",
                    12000,
                    4.7f,
                    98,
                    "mie",
                    true,
                    false
                )
            )
            list.add(
                FoodItem(
                    "4",
                    "Soto Ayam",
                    "🥘",
                    "Kantin 2",
                    "Soto bening dengan ayam suwir dan telur rebus",
                    11000,
                    4.6f,
                    87,
                    "kuah",
                    false,
                    false
                )
            )
            list.add(
                FoodItem(
                    "5",
                    "Ayam Bakar Kecap",
                    "🍗",
                    "Kantin Utama",
                    "Ayam bakar dengan bumbu kecap manis special",
                    18000,
                    4.8f,
                    145,
                    "lauk",
                    true,
                    false
                )
            )
            list.add(
                FoodItem(
                    "6",
                    "Gado-gado Komplit",
                    "🥗",
                    "Kantin 3",
                    "Sayuran segar dengan bumbu kacang istimewa",
                    10000,
                    4.5f,
                    67,
                    "sayur",
                    false,
                    false
                )
            )
            list.add(
                FoodItem(
                    "7",
                    "Nasi Uduk",
                    "🍚",
                    "Kantin Utama",
                    "Nasi uduk dengan lauk pauk lengkap",
                    12000,
                    4.6f,
                    78,
                    "nasi",
                    false,
                    true
                )
            )
            list.add(
                FoodItem(
                    "8",
                    "Bakso Urat",
                    "🍲",
                    "Kantin 3",
                    "Bakso urat jumbo dengan kuah kaldu gurih",
                    12000,
                    4.7f,
                    112,
                    "kuah",
                    true,
                    false
                )
            )
            list.add(
                FoodItem(
                    "9",
                    "Mie Goreng Seafood",
                    "🦐",
                    "Kantin 2",
                    "Mie goreng dengan udang, cumi, dan sayuran",
                    15000,
                    4.5f,
                    56,
                    "mie",
                    false,
                    true
                )
            )
            list.add(
                FoodItem(
                    "10",
                    "Pecel Lele",
                    "🐟",
                    "Kantin 3",
                    "Lele goreng crispy dengan sambal pecel",
                    14000,
                    4.4f,
                    89,
                    "lauk",
                    false,
                    false
                )
            )
            list.add(
                FoodItem(
                    "11",
                    "Es Teh Manis",
                    "🧋",
                    "Kantin Minum",
                    "Teh segar dengan es batu dan gula pilihan",
                    5000,
                    4.9f,
                    312,
                    "minuman",
                    true,
                    false
                )
            )
            list.add(
                FoodItem(
                    "12",
                    "Jus Alpukat",
                    "🥑",
                    "Kantin Minum",
                    "Jus alpukat creamy dengan susu cokelat",
                    10000,
                    4.8f,
                    167,
                    "minuman",
                    true,
                    false
                )
            )
            list.add(
                FoodItem(
                    "13",
                    "Es Campur",
                    "🧊",
                    "Kantin Minum",
                    "Es campur dengan berbagai topping segar",
                    8000,
                    4.6f,
                    78,
                    "minuman",
                    false,
                    false
                )
            )
            list.add(
                FoodItem(
                    "14",
                    "Kopi Susu",
                    "☕",
                    "Kantin Minum",
                    "Kopi susu kekinian ala cafe dengan cold brew",
                    8000,
                    4.7f,
                    203,
                    "minuman",
                    true,
                    true
                )
            )
            list.add(
                FoodItem(
                    "15",
                    "Pisang Goreng",
                    "🍌",
                    "Kantin Snack",
                    "Pisang goreng crispy dengan topping cokelat keju",
                    7000,
                    4.5f,
                    94,
                    "snack",
                    false,
                    false
                )
            )
            list.add(
                FoodItem(
                    "16",
                    "Martabak Mini",
                    "🥞",
                    "Kantin Snack",
                    "Martabak mini isi keju cokelat dan kacang",
                    8000,
                    4.6f,
                    71,
                    "snack",
                    false,
                    true
                )
            )
            list.add(
                FoodItem(
                    "17",
                    "Cireng Isi",
                    "🟡",
                    "Kantin Snack",
                    "Cireng isi dengan berbagai pilihan rasa",
                    5000,
                    4.4f,
                    58,
                    "snack",
                    false,
                    false
                )
            )
            list.add(
                FoodItem(
                    "18",
                    "Nasi Kuning",
                    "🌟",
                    "Kantin Utama",
                    "Nasi kuning gurih dengan lauk tahu tempe dan ayam",
                    11000,
                    4.5f,
                    63,
                    "nasi",
                    false,
                    true
                )
            )
            return list
        }

    val categories: List<String>
        get() {
            val cats: MutableList<String> =
                ArrayList()
            cats.add("semua")
            cats.add("nasi")
            cats.add("mie")
            cats.add("kuah")
            cats.add("lauk")
            cats.add("sayur")
            cats.add("minuman")
            cats.add("snack")
            return cats
        }

    val notifications: List<NotificationItem>
        get() {
            val list: MutableList<NotificationItem> =
                ArrayList()
            list.add(
                NotificationItem(
                    "Pesanan Diproses! 🎉",
                    "Pesanan #KTN-1042 kamu sedang diproses oleh Kantin Utama", "Baru saja", "order"
                )
            )
            list.add(
                NotificationItem(
                    "Promo Hari Ini 🔥",
                    "Diskon 20% semua menu minuman sampai jam 14.00!", "10 menit lalu", "promo"
                )
            )
            list.add(
                NotificationItem(
                    "Pesanan Selesai ✅",
                    "Pesanan #KTN-1038 sudah siap diambil di Kantin 2", "1 jam lalu", "order"
                )
            )
            list.add(
                NotificationItem(
                    "Menu Baru Tersedia 🍜",
                    "Mie Goreng Seafood kini tersedia di Kantin 2. Yuk coba!", "2 jam lalu", "info"
                )
            )
            list.add(
                NotificationItem(
                    "Flash Sale! ⚡",
                    "Nasi Goreng Spesial hanya Rp 10.000 hari ini!", "Kemarin", "promo"
                )
            )
            return list
        }
}
