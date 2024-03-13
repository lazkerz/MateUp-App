package com.example.apps_magang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.presenter.UserPresenter
import com.example.apps_magang.auth.view.user_view
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.apps_magang.core.utils.RealmManager
import com.example.apps_magang.core.utils.ResultState
import io.realm.Realm

class MainActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(
            this,
            this)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigation)

        val navController =
            supportFragmentManager.findFragmentById(R.id.navHost)!!
                .findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_ingredients,
                R.id.navigation_product
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        // Cek apakah pengguna berada di halaman dashboard
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost)
        val currentDestination = navHostFragment?.findNavController()?.currentDestination?.id

        if (currentDestination == R.id.navigation_dashboard) {
            // Jika berada di halaman dashboard, biarkan pengguna kembali ke halaman sebelumnya atau keluar dari aplikasi
            super.onBackPressed()
        } else {
            // Jika tidak berada di halaman dashboard, biarkan perilaku kembali standar
            super.onBackPressed()
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