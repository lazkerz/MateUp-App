package com.example.apps_magang.auth.presentation

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
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
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm

class SignUpActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(this)
        val userModel = UserModel()

        val Name = findViewById<EditText>(R.id.authNameEditText)
        val Usn = findViewById<EditText>(R.id.authUserNameEditText)
        val Password = findViewById<EditText>(R.id.authPasswordEditText)
        val ConfirmPassword = findViewById<EditText>(R.id.authConfirmPasswordEditText)


        val buttonRegis = findViewById<FrameLayout>(R.id.btn_regis)
        val buttonLogin = findViewById<Button>(R.id.btn_login)


        val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)
        val editpassword = authPasswordTextLayout.editText
        editpassword?.transformationMethod = null // Teks password ditampilkan sebagai teks biasa

        authPasswordTextLayout.setEndIconOnClickListener {
            // Mengubah visibilitas teks password sesuai status saat ini
            val editpassword = authPasswordTextLayout.editText
            editpassword?.let {
                if (editpassword.transformationMethod == null) {
                    // Jika teks password ditampilkan, sembunyikan
                    editpassword.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    // Jika teks password disembunyikan, tampilkan
                    editpassword.transformationMethod = null
                }
                // Memastikan teks kembali ke posisi terakhir
                editpassword.setSelection(editpassword.text.length)
            }
        }


        val authConfirmPasswordTextLayout = findViewById<TextInputLayout>(R.id.authConfirmPasswordTextLayout)
        val editText = authConfirmPasswordTextLayout.editText
        editText?.transformationMethod = null // Teks password ditampilkan sebagai teks biasa

        authConfirmPasswordTextLayout.setEndIconOnClickListener {
            // Mengubah visibilitas teks password sesuai status saat ini
            val editText = authConfirmPasswordTextLayout.editText
            editText?.let {
                if (editText.transformationMethod == null) {
                    // Jika teks password ditampilkan, sembunyikan
                    editText.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    // Jika teks password disembunyikan, tampilkan
                    editText.transformationMethod = null
                }
                // Memastikan teks kembali ke posisi terakhir
                editText.setSelection(editText.text.length)
            }
        }




        Name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val name = s.toString()
                if (name.isBlank()) {
                    findViewById<TextInputLayout>(R.id.authNameTextLayout).helperText = "Required*"
                } else {
                    findViewById<TextInputLayout>(R.id.authNameTextLayout).helperText = ""
                }
            }
        })

        Usn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val username = s.toString()
                val userModel = UserModel()

                val usnLayout = findViewById<TextInputLayout>(R.id.authUsernameTextLayout)
                if (username.isBlank()) {
                    usnLayout.helperText= "Required*"
                } else if (username == userModel.username) {
                    usnLayout.error = "Use another username"
                }else {
                    usnLayout.helperText = null
                }
            }
        })

        Password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                val userModel = UserModel()

                val passwordLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)

                if (password.isBlank()) {
                    passwordLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
                    passwordLayout.helperText = "Required*"
                    passwordLayout.error = null // Menghapus pesan kesalahan jika ada
                } else if (!userModel.isPasswordValid(password)) {
                    passwordLayout.error = "Password must be at least 6 characters with at least 1 number"
                    passwordLayout.helperText = null // Menghapus pesan bantuan jika password tidak valid
                } else {
                    passwordLayout.error = null
                    passwordLayout.helperText = "Good Password"
                    passwordLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN))
                }
            }
        })


        ConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val confirmPassword = s.toString()
                val password = Password.text.toString()

                val confirmPasswordLayout = findViewById<TextInputLayout>(R.id.authConfirmPasswordTextLayout)

                if (confirmPassword.isBlank()) {
                    confirmPasswordLayout.helperText = "Required*"
                    confirmPasswordLayout.error = null // Menghapus pesan kesalahan jika ada
                } else if (confirmPassword != password) {
                    confirmPasswordLayout.error = "Password do not match"
                    confirmPasswordLayout.helperText = null // Menghapus pesan bantuan jika password tidak valid
                } else {
                    confirmPasswordLayout.error = null
                    confirmPasswordLayout.helperText = null // Menghapus pesan bantuan jika password valid
                }
            }
        })


        buttonRegis.setOnClickListener {
            val name = Name.text.toString()
            val username = Usn.text.toString()
            val password = Password.text.toString()
            val skinType = findViewById<Spinner>(R.id.spActivity).selectedItem.toString()

            when {
                name.isBlank() || username.isBlank() || password.isBlank() -> {
                    // Tampilkan pesan jika ada input yang kosong
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Panggil fungsi addUser jika semua kondisi terpenuhi
                    presenter.addUser(name, username, password, skinType) { isSuccess ->
                        if (isSuccess) {
                            // Tampilkan pesan jika pendaftaran berhasil
                            Toast.makeText(this, "Registration successful. Please proceed to login.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, SignInActivity::class.java))
                            finish()
                        } else {
                            // Tampilkan pesan jika pendaftaran gagal
                            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
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
