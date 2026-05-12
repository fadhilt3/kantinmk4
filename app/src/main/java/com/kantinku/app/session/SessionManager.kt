package com.kantinku.app.session

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

class SessionManager private constructor(context: Context) {
    private val prefs: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    fun saveLogin(name: String, email: String?, username: String?) {
        editor.putBoolean(KEY_LOGGED, true)
        editor.putString(KEY_NAME, name)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_USERNAME, username)
        // Generate initials for avatar
        val parts = name.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val initials = if (parts.size >= 2)
            parts[0][0].toString() + parts[1][0].toString()
        else parts[0][0].toString()
        editor.putString(KEY_AVATAR, initials.uppercase(Locale.getDefault()))
        editor.apply()
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }

    val isLoggedIn: Boolean
        get() = prefs.getBoolean(
            KEY_LOGGED,
            false
        )
    val userName: String
        get() = prefs.getString(KEY_NAME, "")!!
    val userEmail: String
        get() = prefs.getString(KEY_EMAIL, "")!!
    val username: String
        get() = prefs.getString(KEY_USERNAME, "")!!
    val avatar: String
        get() = prefs.getString(KEY_AVATAR, "?")!!

    val firstName: String
        /** First name only for greeting  */
        get() {
            val name = userName
            if (name.isEmpty()) return "Kamu"
            return name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        }

    companion object {
        private const val PREF_NAME = "KantinKuSession"
        private const val KEY_LOGGED = "isLoggedIn"
        private const val KEY_NAME = "userName"
        private const val KEY_EMAIL = "userEmail"
        private const val KEY_USERNAME = "userUsername"
        private const val KEY_PHONE = "userPhone"
        private const val KEY_AVATAR = "userAvatar"

        private var instance: SessionManager? = null
        @JvmStatic
        fun getInstance(context: Context): SessionManager {
            if (instance == null) instance = SessionManager(context)
            return instance!!
        }
    }
}
