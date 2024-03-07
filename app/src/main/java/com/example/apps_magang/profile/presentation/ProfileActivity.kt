package com.example.apps_magang.profile.presentation

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apps_magang.R
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import io.realm.Realm

class ProfileActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(
            this)

        val Name= findViewById<TextView>(R.id.authNameTextView)
        val Usn= findViewById<TextView>(R.id.authUsernameTextView)
        val Password = findViewById<TextView>(R.id.authPasswordTextView)
        val SkinType = findViewById<Spinner>(R.id.spActivity)
        val buttonSubmit = findViewById<FrameLayout>(R.id.btn_submit)
        val back = findViewById<ImageView>(R.id.ic_back)

        buttonSubmit.setOnClickListener {
            val skinType = SkinType.selectedItem.toString()
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