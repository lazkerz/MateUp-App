package com.example.apps_magang.auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.example.apps_magang.MainActivity
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.utils.LoginManager
import com.example.apps_magang.utils.RealmManager
import com.example.apps_magang.utils.ResultState
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm

class SignInActivity : AppCompatActivity(), user_view {
    private lateinit var presenter: UserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(
            this,
            this )

        val Usn= findViewById<EditText>(R.id.authUserNameEditText)
        val Password = findViewById<EditText>(R.id.authPasswordEditText)
        val buttonLogin = findViewById<FrameLayout>(R.id.btn_login)
        val buttonRegis = findViewById<Button>(R.id.btn_regis)

        findViewById<TextView>(R.id.tv_name).visibility = View.GONE
        findViewById<EditText>(R.id.authNameEditText).visibility = View.GONE

        val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)

        authPasswordTextLayout.setEndIconOnClickListener {
            // Mengubah visibilitas teks password sesuai status saat ini
            val editText = authPasswordTextLayout.editText
            editText?.let {
                if (editText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    // Jika teks password disembunyikan, tampilkan
                    editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    // Jika teks password ditampilkan, sembunyikan
                    editText.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                // Memastikan teks kembali ke posisi terakhir
                editText.setSelection(editText.text.length)
            }
        }

        buttonLogin.setOnClickListener {
            val username = Usn.text.toString()
            val password = Password.text.toString()

            presenter.login(username, password) { isSuccess ->
                if (isSuccess) {
                    // Jika login berhasil, arahkan ke MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    // Simpan status login
                    LoginManager.saveLogin(this, true)
                    finish()
                } else {
                    // Jika login gagal, tampilkan pesan kesalahan
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }
        buttonRegis.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
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