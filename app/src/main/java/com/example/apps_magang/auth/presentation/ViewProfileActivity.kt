package com.example.apps_magang.auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.core.utils.LoginManager
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import io.realm.Realm

class ViewProfileActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(
            this,
            this)

        val Name = findViewById<TextView>(R.id.tv_name)
        val Usn = findViewById<TextView>(R.id.tv_username)
        val SkinType = findViewById<TextView>(R.id.tv_skin_type)
        val Edit = findViewById<FrameLayout>(R.id.btn_edit)
        val back = findViewById<ImageView>(R.id.ic_back)
        val out = findViewById<ImageView>(R.id.ic_logout)

        val userModel = presenter.getUser()
        Log.d("Profile", "Data User: ${userModel?.skinType}")

        if (userModel != null) {

            Name.text = userModel.name
            Usn.text = userModel.username
            SkinType.text = userModel.skinType

        }


        Edit.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        back.setOnClickListener {
            onBackPressed()
        }
        out.setOnClickListener {
            presenter.logout()
            checkLoginStatus()
            navigateToLogin()
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = LoginManager.isLoggedIn(this)
        Log.d("LoginStatus", "Is user logged in after logout: $isLoggedIn")
    }

    private fun navigateToLogin() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish() // Selesaikan activity saat ini agar pengguna tidak dapat kembali ke halaman profil tanpa login ulang
    }

    override fun displayUser(result: ResultState<UserModel>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val userData = result.data
            }
            is ResultState.Error -> {
                // Handle jika terjadi error
                val errorMessage = result.error
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                // Handle loading state
                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}