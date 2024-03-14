package com.example.apps_magang.auth.presentation

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.core.utils.LoginManager
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm

class ProfileActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserPresenter
    private lateinit var userModel: UserModel

    private var isSpinnerSelected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        Realm.init(this)
        RealmManager.initRealm()

        // Perbarui UI dengan data pengguna


        presenter = UserPresenter(this, this)

        val nameEditText = findViewById<EditText>(R.id.authNameEditText)
        val usernameEditText = findViewById<EditText>(R.id.authUserNameEditText)
        val passwordEditText = findViewById<EditText>(R.id.authPasswordEditText)
        val skinTypeSpinner = findViewById<Spinner>(R.id.spActivity)
        val submitButton = findViewById<FrameLayout>(R.id.btn_submit)
        val backButton = findViewById<ImageView>(R.id.ic_back)


        findViewById<TextInputLayout>(R.id.authConfirmPasswordTextLayout).visibility = View.GONE
        findViewById<EditText>(R.id.authConfirmPasswordEditText).visibility = View.GONE
        findViewById<TextView>(R.id.tv_confirm_password).visibility = View.GONE


        backButton.setOnClickListener {
            onBackPressed()
        }

        setupUI()

        val user = presenter.getUser()
        Log.d("Profile", "Data User: ${user?.skinType}")

        if (user != null) {
            userModel = user
            nameEditText.setText(user.name)
            usernameEditText.setText(user.username)
            passwordEditText.setText(user.password)

            user.skinType?.let { skinType ->
                val skinTypeIndex = getIndexOfSkinType(userModel.skinType ?: "")
                skinTypeSpinner.setSelection(skinTypeIndex, false) // Set skinType di Spinner dengan menggunakan indeks, animate=false
                // Set skinType di Spinner dengan menggunakan indeks
            }
        } else{
            userModel =  UserModel()
        }

        updateUserUI(userModel)

        // Listener untuk spinner
        skinTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Set isSpinnerSelected ke true saat ada item yang dipilih di spinner
                isSpinnerSelected = position != 0
                // Panggil validateAndEnableSubmitButton() setiap kali ada perubahan di spinner
                validateAndEnableSubmitButton()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Set isSpinnerSelected ke false jika tidak ada item yang dipilih di spinner
                isSpinnerSelected = false
                // Panggil validateAndEnableSubmitButton() setiap kali ada perubahan di spinner
                validateAndEnableSubmitButton()
            }
        }

        validateAndEnableSubmitButton()
    }

    private fun setupUI() {
        val nameEditText = findViewById<EditText>(R.id.authNameEditText)
        val usernameEditText = findViewById<EditText>(R.id.authUserNameEditText)
        val passwordEditText = findViewById<EditText>(R.id.authPasswordEditText)
        val submitButton = findViewById<FrameLayout>(R.id.btn_submit)

        // Hapus pesan kesalahan di awal
        findViewById<TextInputLayout>(R.id.authNameTextLayout).error = null
        findViewById<TextInputLayout>(R.id.authUsernameTextLayout).error = null
        findViewById<TextInputLayout>(R.id.authPasswordTextLayout).error = null

        // Hapus pesan bantuan di awal
        findViewById<TextInputLayout>(R.id.authNameTextLayout).helperText = null
        findViewById<TextInputLayout>(R.id.authUsernameTextLayout).helperText = null
        findViewById<TextInputLayout>(R.id.authPasswordTextLayout).helperText = null

        // TextWatcher untuk memantau perubahan di EditText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateAndEnableSubmitButton()
            }
        }

        // Tambahkan TextWatcher ke EditText
        nameEditText.addTextChangedListener(textWatcher)
        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        submitButton.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun updateUserProfile() {

        val name = findViewById<EditText>(R.id.authNameEditText).text.toString()
        val username = findViewById<EditText>(R.id.authUserNameEditText).text.toString()
        val Password = findViewById<EditText>(R.id.authPasswordEditText)
        val skinType = findViewById<Spinner>(R.id.spActivity).selectedItem.toString()

        val password = Password.text.toString()

        if (name.isNotBlank() && username.isNotBlank() && password.isNotBlank() && skinType.isNotBlank()) {
            // Cek apakah ada perubahan dibuat
            if (name != userModel.name || username != userModel.username || password != userModel.password || skinType != userModel.skinType) {
                // Lakukan pembaruan data pengguna jika ada perubahan
                userModel.apply {
                    this.name = name
                    this.username = username
                    this.password = password
                    this.skinType = skinType
                }
                presenter.editUser(userModel.id!!, name, username, password, skinType) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this, "User profile updated successfully. Please login again", Toast.LENGTH_SHORT).show()
                        // Lakukan logout dan navigasi ke halaman login
                        presenter.logout()
                        navigateToLogin()
                    } else {
                        Toast.makeText(this, "Failed to update user profile. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Tampilkan toast jika tidak ada perubahan dibuat
                Toast.makeText(this, "Nothing updated", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Tampilkan pesan required jika ada bidang yang kosong
            val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)
            findViewById<TextInputLayout>(R.id.authNameTextLayout).error = if (name.isBlank()) "Required" else null
            findViewById<TextInputLayout>(R.id.authUsernameTextLayout).error = if (username.isBlank()) "Required" else null
            authPasswordTextLayout.error = if (password.isBlank()) "Required" else null

            val passwordWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                val userModel = UserModel()

                if (!userModel.isPasswordValid(password)) {
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
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }



    private fun validateAndEnableSubmitButton() {
        val nameEditText = findViewById<EditText>(R.id.authNameEditText)
        val usernameEditText = findViewById<EditText>(R.id.authUserNameEditText)
        val passwordEditText = findViewById<EditText>(R.id.authPasswordEditText)
        val skinTypeSpinner = findViewById<Spinner>(R.id.spActivity)
        val submitButton = findViewById<FrameLayout>(R.id.btn_submit)

        // Periksa apakah ada perubahan di field name, username, password, atau skinType
        val isNameChanged = nameEditText.text.toString() != userModel.name
        val isUsernameChanged = usernameEditText.text.toString() != userModel.username
        val isPasswordChanged = passwordEditText.text.toString() != userModel.password
        val isSkinTypeSelected = skinTypeSpinner.selectedItemPosition != 0

        // Aktifkan tombol submit jika ada perubahan di salah satu dari field name, username, password, atau skinType
        submitButton.isEnabled = isNameChanged || isUsernameChanged || isPasswordChanged || isSkinTypeSelected

        // Set background color of the button when disabled
        if (submitButton.isEnabled) {
            submitButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.Primary_50))
        } else {
            submitButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.light_grey))
        }
    }


    private fun getIndexOfSkinType(skinType: String): Int {
        return when (skinType) {
            "Sensitive" -> 0
            "Normal" -> 1
            "Oily" -> 2
            "Dry" -> 3
            else -> 0 // Default to Sensitive if skin type is unknown
        }
    }

    private fun updateUserUI(userModel: UserModel) {
        val nameEditText = findViewById<EditText>(R.id.authNameEditText)
        val usernameEditText = findViewById<EditText>(R.id.authUserNameEditText)
        val passwordEditText = findViewById<EditText>(R.id.authPasswordEditText)
        val skinTypeSpinner = findViewById<Spinner>(R.id.spActivity)

        nameEditText.setText(userModel.name)
        usernameEditText.setText(userModel.username)
        passwordEditText.setText(userModel.password)

        // Set skinType di Spinner dengan menggunakan indeks
        val skinTypeIndex = when (userModel.skinType) {
            "Sensitive" -> 0
            "Normal" -> 1
            "Oily" -> 2
            "Dry" -> 3
            else -> 0 // Default to Sensitive if skin type is unknown
        }
        skinTypeSpinner.setSelection(skinTypeIndex)
    }
    private fun navigateToLogin() {
        val intent = Intent(this, SignInActivity::class.java)

        startActivity(intent)
        finish() // Finish the current activity so the user cannot return to the profile page without logging in again
    }

    override fun displayUser(result: ResultState<UserModel>) {
        when (result) {
            is ResultState.Success -> {
                val userData = result.data
                updateUserUI(userData)
            }
            is ResultState.Error -> {
                val errorMessage = result.error
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
