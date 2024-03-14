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
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.apps_magang.MainActivity
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.dashboard.presentation.DashboardFragment
import com.example.apps_magang.core.utils.LoginManager
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm

class SignInActivity : AppCompatActivity(), user_view {
    private lateinit var presenter: UserPresenter
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(
            this,
            this)

        val Usn = findViewById<EditText>(R.id.authUserNameEditText)
        val Password = findViewById<EditText>(R.id.authPasswordEditText)
        val buttonLogin = findViewById<FrameLayout>(R.id.btn_login)
        val buttonRegis = findViewById<Button>(R.id.btn_regis)


        findViewById<TextView>(R.id.tv_name).visibility = View.GONE
        findViewById<EditText>(R.id.authNameEditText).visibility = View.GONE
        findViewById<Spinner>(R.id.spActivity).visibility = View.GONE
        findViewById<TextView>(R.id.tv_skinType).visibility = View.GONE
        findViewById<TextView>(R.id.tv_confirm_password).visibility = View.GONE
        findViewById<TextView>(R.id.authConfirmPasswordEditText).visibility = View.GONE
        findViewById<TextInputLayout>(R.id.authConfirmPasswordTextLayout).visibility = View.GONE
        findViewById<TextInputLayout>(R.id.authNameTextLayout).visibility = View.GONE

        val authUsernameTextLayout = findViewById<TextInputLayout>(R.id.authUsernameTextLayout)
        val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)

        authUsernameTextLayout.helperText = ""
        authPasswordTextLayout.helperText = ""

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
            setLoading(true)
            val username = Usn.text.toString()
            val password = Password.text.toString()

            if (username.isBlank()) {
                authUsernameTextLayout.helperText = "Required*"
            }
            if (password.isBlank()) {
                authPasswordTextLayout.helperText = "Required*"
            }

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

            val passwordwatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val password = s.toString()

                    if (password.isBlank()) {
                        authPasswordTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
                        authPasswordTextLayout.helperText = "Required*"
                        authPasswordTextLayout.error = null // Menghapus pesan kesalahan jika ada
                    } else {
                        authPasswordTextLayout.helperText = null
                        authPasswordTextLayout.error = null
                    }
                }
            }

            Password.addTextChangedListener(passwordwatcher)


            presenter.login(username, password) { isSuccess ->
                if (isSuccess) {
                    // Jika login berhasil, arahkan ke MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    // Simpan status login
                    LoginManager.saveLoginStatus(this, true)
                    finish()
                } else {
                    // Jika login gagal, tampilkan pesan kesalahan
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    setLoading(false)
                }
            }
        }

        buttonRegis.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
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