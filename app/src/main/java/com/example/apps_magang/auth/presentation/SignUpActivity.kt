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
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
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

        presenter = UserPresenter(this, this)

        val Name = findViewById<EditText>(R.id.authNameEditText)
        val Usn = findViewById<EditText>(R.id.authUserNameEditText)
        val Password = findViewById<EditText>(R.id.authPasswordEditText)
        val ConfirmPassword = findViewById<EditText>(R.id.authConfirmPasswordEditText)

        val buttonRegis = findViewById<FrameLayout>(R.id.btn_regis)
        val buttonLogin = findViewById<Button>(R.id.btn_login)

        val authNameTextLayout = findViewById<TextInputLayout>(R.id.authNameTextLayout)
        val authUsernameTextLayout = findViewById<TextInputLayout>(R.id.authUsernameTextLayout)
        val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)
        val authConfirmPasswordTextLayout = findViewById<TextInputLayout>(R.id.authConfirmPasswordTextLayout)

        // Menyembunyikan pesan helper untuk Name dan Username saat awal
        authNameTextLayout.helperText = ""
        authUsernameTextLayout.helperText = ""
        authPasswordTextLayout.helperText = ""
        authConfirmPasswordTextLayout.helperText = ""

        buttonRegis.setOnClickListener {
            setLoading(true)

            val name = Name.text.toString()
            val username = Usn.text.toString()
            val password = Password.text.toString()
            val confirm = ConfirmPassword.text.toString()
            val skinType = findViewById<Spinner>(R.id.spActivity).selectedItem.toString()

            if (name.isBlank()) {
                authNameTextLayout.helperText = "Required*"
            }
            if (username.isBlank()) {
                authUsernameTextLayout.helperText = "Required*"
            }
            if (password.isBlank()) {
                authPasswordTextLayout.helperText = "Required*"
            }
            if (confirm.isBlank()) {
                authConfirmPasswordTextLayout.helperText = "Required*"
            }

            val nameWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val name = s.toString()

                    if (name.isBlank()) {
                        authNameTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
                        authNameTextLayout.helperText = "Required*"
                        authNameTextLayout.error = null // Menghapus pesan kesalahan jika ada
                    } else {
                        authNameTextLayout.helperText = null
                        authNameTextLayout.error = null
                    }
                }
            }

            Name.addTextChangedListener(nameWatcher)

            val usnWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val username = s.toString()

                    if (username.isBlank()) {
                        authUsernameTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
                        authUsernameTextLayout.helperText = "Required*"
                        authUsernameTextLayout.error = null // Menghapus pesan kesalahan jika ada
                    } else {
                        authUsernameTextLayout.helperText = null
                        authUsernameTextLayout.error = null
                    }
                }
            }

            Usn.addTextChangedListener(usnWatcher)

            // Menambahkan TextWatcher untuk Password dan ConfirmPassword
            val passwordWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val password = s.toString()
                    val userModel = UserModel()

                    if (password.isBlank()) {
                        authPasswordTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
                        authPasswordTextLayout.helperText = "Required*"
                        authPasswordTextLayout.error = null // Menghapus pesan kesalahan jika ada
                    } else if (!userModel.isPasswordValid(password)) {
                        authPasswordTextLayout.error = "Password must be at least 6 characters with at least 1 number"
                        authPasswordTextLayout.helperText = null // Menghapus pesan bantuan jika password tidak valid
                    } else {
                        authPasswordTextLayout.error = null
                        authPasswordTextLayout.helperText = "Good Password"
                        authPasswordTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN))
                    }
                }
            }

            Password.addTextChangedListener(passwordWatcher)

            ConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val confirmPassword = s.toString()
                    val password = Password.text.toString()

                    if (confirmPassword.isBlank()) {
                        authConfirmPasswordTextLayout.helperText = "Required*"
                        authConfirmPasswordTextLayout.error = null // Menghapus pesan kesalahan jika ada
                    } else if (confirmPassword != password) {
                        authConfirmPasswordTextLayout.error = "Password do not match"
                        authConfirmPasswordTextLayout.helperText = null // Menghapus pesan bantuan jika password tidak valid
                    } else {
                        authConfirmPasswordTextLayout.error = null
                        authConfirmPasswordTextLayout.helperText = null // Menghapus pesan bantuan jika password valid
                    }
                }
            })

            // Panggil fungsi addUser jika semua kondisi terpenuhi
            if (name.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
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

        buttonLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setLoading(isLoading: Boolean) {
        val viewLoading = findViewById<RelativeLayout>(R.id.view_loading)
        viewLoading?.visibility = if (isLoading) View.VISIBLE else View.GONE
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