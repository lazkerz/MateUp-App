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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(this, this)

        userModel = UserModel()

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
            nameEditText.setText(user.name)
            usernameEditText.setText(user.username)
            passwordEditText.setText(user.password)

            user.skinType?.let { skinType ->
                val skinTypeIndex = getIndexOfSkinType(userModel.skinType ?: "")
                skinTypeSpinner.setSelection(skinTypeIndex, false) // Set skinType di Spinner dengan menggunakan indeks, animate=false
                // Set skinType di Spinner dengan menggunakan indeks
            }
        }
    }

    private fun setupUI() {
        val nameEditText = findViewById<EditText>(R.id.authNameEditText)
        val usernameEditText = findViewById<EditText>(R.id.authUserNameEditText)
        val passwordEditText = findViewById<EditText>(R.id.authPasswordEditText)
        val submitButton = findViewById<FrameLayout>(R.id.btn_submit)

        arrayOf(nameEditText, usernameEditText, passwordEditText).forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    validateAndEnableSubmitButton()
                }
            })
        }

        submitButton.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun updateUserProfile() {
        val name = findViewById<EditText>(R.id.authNameEditText).text.toString()
        val username = findViewById<EditText>(R.id.authUserNameEditText).text.toString()
        val password = findViewById<EditText>(R.id.authPasswordEditText).text.toString()
        val skinType = findViewById<Spinner>(R.id.spActivity).selectedItem.toString()

        if (validateName() || validateUsername() || validatePassword() || validateSkinType()) {
            userModel.id?.let { userId ->
                presenter.editUser(userId, name.takeIf { validateName() }, username.takeIf { validateUsername() },
                    password.takeIf { validatePassword() }, skinType.takeIf { validateSkinType() }) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this, "User profile updated successfully. Please login again", Toast.LENGTH_SHORT).show()
                        userModel.apply {
                            name?.let { this.name = it }
                            username?.let { this.username = it }
                            password?.let { this.password = it }
                            skinType?.let { this.skinType = it }
                        }
                        updateUserUI(userModel)
                        presenter.logout()
                        navigateToLogin()
                    } else {
                        Toast.makeText(this, "Failed to update user profile. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please ensure at least one field is valid.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun validateAndEnableSubmitButton() {
        val nameEditText = findViewById<EditText>(R.id.authNameEditText)
        val usernameEditText = findViewById<EditText>(R.id.authUserNameEditText)
        val passwordEditText = findViewById<EditText>(R.id.authPasswordEditText)
        val skinTypeSpinner = findViewById<Spinner>(R.id.spActivity)
        val submitButton = findViewById<FrameLayout>(R.id.btn_submit)

        val isNameValid = validateName()
        val isUsernameValid = validateUsername()
        val isPasswordValid = validatePassword()
        val isSkinTypeValid = validateSkinType()

        val isValid = isNameValid || isUsernameValid || isPasswordValid || isSkinTypeValid

        submitButton.isEnabled = isValid

        // Change button color based on validation result
        if (isValid) {
            submitButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.Primary_50))
        } else {
            submitButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.light_grey))
        }
    }



    private fun validateName(): Boolean {
        val name = findViewById<EditText>(R.id.authNameEditText).text.toString()
        val isNameValid = name.isNotBlank()
        if (!isNameValid) {
            findViewById<TextInputLayout>(R.id.authNameTextLayout).helperText = "Required*"
        }
        return isNameValid
    }

    private fun validateUsername(): Boolean {
        val username = findViewById<EditText>(R.id.authUserNameEditText).text.toString()
        val isUsernameValid = username.isNotBlank()
        if (!isUsernameValid) {
            findViewById<TextInputLayout>(R.id.authUsernameTextLayout).helperText = "Required*"
        }
        return isUsernameValid
    }

    private fun validatePassword(): Boolean {
        val password = findViewById<EditText>(R.id.authPasswordEditText).text.toString()
        val isPasswordValid = password.isNotBlank()
        if (!isPasswordValid) {
            findViewById<TextInputLayout>(R.id.authPasswordTextLayout).helperText = "Required*"
        }
        return isPasswordValid
    }

    private fun validateSkinType(): Boolean {
        val skinTypeSpinner = findViewById<Spinner>(R.id.spActivity)
        val selectedSkinType = skinTypeSpinner.selectedItem.toString()
        val isSkinTypeValid = selectedSkinType.isNotBlank()
        if (!isSkinTypeValid) {
            Toast.makeText(this, "Please select a skin type", Toast.LENGTH_SHORT).show()
        }
        return isSkinTypeValid
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

        val skinTypeIndex = getIndexOfSkinType(userModel.skinType ?: "")
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
