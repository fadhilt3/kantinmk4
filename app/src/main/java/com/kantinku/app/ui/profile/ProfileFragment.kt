package com.kantinku.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kantinku.app.R
import com.kantinku.app.session.SessionManager.Companion.getInstance
import com.kantinku.app.ui.auth.LoginActivity
import com.kantinku.app.ui.history.HistoryActivity
import com.kantinku.app.ui.notification.NotificationActivity
import com.kantinku.app.utils.CartManager

class ProfileFragment : Fragment() {
    override fun onCreateView(inf: LayoutInflater, c: ViewGroup?, s: Bundle?): View? {
        return inf.inflate(R.layout.fragment_profile, c, false)
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)

        val sm = getInstance(requireContext())
        (v.findViewById<View>(R.id.tv_avatar) as TextView).text = sm.avatar
        (v.findViewById<View>(R.id.tv_name) as TextView).text = sm.userName
        (v.findViewById<View>(R.id.tv_email) as TextView).text = sm.userEmail
        (v.findViewById<View>(R.id.tv_username_badge) as TextView).text =
            "@" + sm.username

        v.findViewById<View>(R.id.row_history).setOnClickListener { x: View? ->
            startActivity(
                Intent(
                    requireContext(),
                    HistoryActivity::class.java
                )
            )
        }
        v.findViewById<View>(R.id.row_notif).setOnClickListener { x: View? ->
            startActivity(
                Intent(
                    requireContext(),
                    NotificationActivity::class.java
                )
            )
        }
        v.findViewById<View>(R.id.row_favorites).setOnClickListener { x: View? ->
            Toast.makeText(
                requireContext(),
                "Fitur favorit segera hadir! ❤️",
                Toast.LENGTH_SHORT
            ).show()
        }
        v.findViewById<View>(R.id.row_settings).setOnClickListener { x: View? ->
            Toast.makeText(
                requireContext(),
                "Pengaturan segera hadir! ⚙️",
                Toast.LENGTH_SHORT
            ).show()
        }
        v.findViewById<View>(R.id.row_about).setOnClickListener { x: View? ->
            Toast.makeText(
                requireContext(),
                "KantinKu v2.0 - Modern Canteen App 🍽️",
                Toast.LENGTH_SHORT
            ).show()
        }
        v.findViewById<View>(R.id.row_logout).setOnClickListener { x: View? ->
            CartManager.instance.clearCart()
            sm.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}
