package com.example.apps_magang.auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.core.utils.LoginManager
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import io.realm.Realm

class confirmationActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(
            this,
            this)

        val yes = findViewById<Button>(R.id.btn_yes)
        val no = findViewById<Button>(R.id.btn_no)

        yes.setOnClickListener {
            presenter.logout()
            checkLoginStatus()
            navigateToLogin()
        }

        no.setOnClickListener {
            val intent = Intent(this, ViewProfileActivity::class.java)
            startActivity(intent)
            finish()
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