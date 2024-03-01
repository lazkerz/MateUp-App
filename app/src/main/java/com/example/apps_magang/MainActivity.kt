package com.example.apps_magang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.apps_magang.auth.model.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.presentation.SignInActivity
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.utils.ResultState

class MainActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = UserPresenter(
            this,
            this )

        val logout = findViewById<ImageView>(R.id.ic_out)

        logout.setOnClickListener {
            presenter.logout()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish() // Optional: Menutup aktivitas saat ini setelah logout
        }

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