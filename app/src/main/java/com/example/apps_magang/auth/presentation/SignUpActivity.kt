package com.example.apps_magang.auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import io.realm.Realm

class SignUpActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(
            this)

        val Name= findViewById<EditText>(R.id.authNameEditText)
        val Usn= findViewById<EditText>(R.id.authUserNameEditText)
        val Password = findViewById<EditText>(R.id.authPasswordEditText)
        val SkinType = findViewById<Spinner>(R.id.spActivity)
        val buttonRegis = findViewById<FrameLayout>(R.id.btn_regis)
        val buttonLogin = findViewById<Button>(R.id.btn_login)
        val back = findViewById<ImageView>(R.id.ic_back)

        buttonRegis.setOnClickListener {
            val name = Name.text.toString()
            val username = Usn.text.toString()
            val password = Password.text.toString()
            val skinType = SkinType.selectedItem.toString()

            val userModel = UserModel()

            when {
                name.isEmpty() -> {
                    // Tampilkan pesan jika nama kosong
                    Toast.makeText(this, "Nama harus diisi", Toast.LENGTH_SHORT).show()
                }
                username.isEmpty() -> {
                    // Tampilkan pesan jika username kosong
                    Toast.makeText(this, "Username harus diisi", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    // Tampilkan pesan jika password kosong
                    Toast.makeText(this, "Password harus diisi", Toast.LENGTH_SHORT).show()
                }
                !userModel.isPasswordValid(password) -> {
                    // Tampilkan pesan jika password tidak memenuhi kriteria
                    Toast.makeText(this, "Password minimal 6 karakter dengan setidaknya satu huruf dan angka", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Panggil fungsi addUser jika semua kondisi terpenuhi
                    presenter.addUser(name, username, password, skinType) { isSuccess ->
                        if (isSuccess) {
                            // Tampilkan pesan jika pendaftaran berhasil
                            Toast.makeText(this, "Berhasil mendaftar. Silakan login.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, SignInActivity::class.java))
                            finish()
                        } else {
                            // Tampilkan pesan jika pendaftaran gagal
                            Toast.makeText(this, "Gagal mendaftar. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        buttonLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
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