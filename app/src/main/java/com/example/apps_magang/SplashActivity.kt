package com.example.apps_magang

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.apps_magang.auth.presentation.SignInActivity
import com.example.apps_magang.core.utils.LoginManager
import com.example.apps_magang.core.utils.RealmManager
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var splashScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(this)
        RealmManager.initRealm()

        splashScope = CoroutineScope(Dispatchers.Main)
        splashScope.launch {
            delaySplashScreen()
        }
    }

    private suspend fun delaySplashScreen() {
        delay(1000) // Tampilkan splash screen selama 1 detik
        navigateToAppropriateScreen()
    }

    private fun navigateToAppropriateScreen() {
        val isLoggedIn = LoginManager.isLoggedIn(this)
        val destination = if (isLoggedIn) {
            MainActivity::class.java
        } else {
            SignInActivity::class.java
        }
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        splashScope.cancel()
    }
}
