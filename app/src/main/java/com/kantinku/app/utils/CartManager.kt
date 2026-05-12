package com.kantinku.app.utils

import com.kantinku.app.model.FoodItem

class CartManager private constructor() {

    interface CartListener {
        fun onCartChanged()
    }

    class CartItem(var food: FoodItem, var qty: Int)

    private val items: MutableMap<String, CartItem> = LinkedHashMap()
    private var listener: CartListener? = null

    fun setListener(l: CartListener?) {
        listener = l
    }

    private fun notifyCart() {
        listener?.onCartChanged()
    }

    fun addItem(food: FoodItem) {
        if (items.containsKey(food.id)) {
            items[food.id]?.qty = items[food.id]?.qty?.plus(1) ?: 1
        } else {
            items[food.id] = CartItem(food, 1)
        }
        notifyCart()
    }

    fun removeOne(id: String) {
        val ci = items[id] ?: return

        if (ci.qty > 1) {
            ci.qty--
        } else {
            items.remove(id)
        }

        notifyCart()
    }

    fun removeAll(id: String) {
        items.remove(id)
        notifyCart()
    }

    fun clearCart() {
        items.clear()
        notifyCart()
    }

    fun getItems(): List<CartItem> {
        return ArrayList(items.values)
    }

    val isEmpty: Boolean
        get() = items.isEmpty()

    val totalQty: Int
        get() {
            var total = 0
            for (c in items.values) {
                total += c.qty
            }
            return total
        }

    val subtotal: Int
        get() {
            var total = 0
            for (c in items.values) {
                total += c.food.price * c.qty
            }
            return total
        }

    val total: Int
        get() = subtotal + serviceFee

    companion object {

        private var INSTANCE: CartManager? = null

        val instance: CartManager
            get() {
                if (INSTANCE == null) {
                    INSTANCE = CartManager()
                }
                return INSTANCE!!
            }

        const val serviceFee = 2000

        fun rupiah(amount: Int): String {
            val s = amount.toString()
            val r = StringBuilder()
            var cnt = 0

            for (i in s.length - 1 downTo 0) {
                if (cnt > 0 && cnt % 3 == 0) {
                    r.insert(0, ".")
                }
                r.insert(0, s[i])
                cnt++
            }

            return "Rp $r"
        }
    }
}