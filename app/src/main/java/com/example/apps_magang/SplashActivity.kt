package com.example.apps_magang

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.apps_magang.auth.presentation.SignInActivity
import com.example.apps_magang.core.utils.LoginManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var splashScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScope = CoroutineScope(Dispatchers.Main)
        splashScope.launch {
            delay(1000) // Tampilkan splash screen selama 1 detik

            val isLoggedIn = LoginManager.isLoggedIn()
            val user = if (isLoggedIn) {
                MainActivity::class.java
            } else {
                SignInActivity::class.java
            }
            val intent = Intent(this@SplashActivity, user)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        splashScope.cancel()
    }
}
