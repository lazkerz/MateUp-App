package com.example.apps_magang.auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.utils.RealmManager
import com.example.apps_magang.utils.ResultState
import io.realm.Realm

class SignUpActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(
            this,
            this )

        val Name= findViewById<EditText>(R.id.authNameEditText)
        val Usn= findViewById<EditText>(R.id.authUserNameEditText)
        val Password = findViewById<EditText>(R.id.authPasswordEditText)
        val buttonRegis = findViewById<FrameLayout>(R.id.btn_regis)
        val buttonLogin = findViewById<Button>(R.id.btn_login)
        val back = findViewById<ImageView>(R.id.ic_back)

        buttonRegis.setOnClickListener {
            val name = Name.text.toString()
            val username = Usn.text.toString()
            val password = Password.text.toString()

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
            } else {
                // Semua input terisi, lakukan proses pendaftaran
                presenter.addUser(name, username, password) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this, "Berhasil mendaftar. Silakan login.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SignInActivity::class.java))
                        finish()
                    } else {
                        // Jika pendaftaran gagal, tampilkan pesan kesalahan
                        Toast.makeText(this, "Gagal mendaftar. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        buttonLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        back.setOnClickListener {
            onBackPressed()
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