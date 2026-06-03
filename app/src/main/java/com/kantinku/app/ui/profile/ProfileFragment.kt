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
import com.kantinku.app.utils.CartManager
import com.kantinku.app.ui.favorite.FavoriteActivity
import com.kantinku.app.ui.about.AboutActivity

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
        (v.findViewById<View>(R.id.tv_username_badge) as TextView).text = "@" + sm.username

        v.findViewById<View>(R.id.row_history).setOnClickListener {
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }
        v.findViewById<View>(R.id.row_favorites).setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteActivity::class.java))
        }
        v.findViewById<View>(R.id.row_about).setOnClickListener {
            startActivity(Intent(requireContext(), AboutActivity::class.java))
        }
        v.findViewById<View>(R.id.row_logout).setOnClickListener {
            CartManager.instance.clearCart()
            sm.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}