//package com.example.apps_magang.auth.presentation
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.FrameLayout
//import android.widget.ImageView
//import android.widget.Spinner
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContentProviderCompat.requireContext
//import com.example.apps_magang.R
//import com.example.apps_magang.auth.model.database.UserModel
//import com.example.apps_magang.auth.presenter.UserPresenter
//import com.example.apps_magang.auth.view.user_view
//import com.example.apps_magang.core.utils.LoginManager
//import com.example.apps_magang.core.utils.RealmManager
//import com.example.apps_magang.core.utils.ResultState
//import io.realm.Realm
//
//class ProfileActivity : AppCompatActivity(), user_view {
//
//    private lateinit var presenter: UserPresenter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
//
//        Realm.init(this)
//        RealmManager.initRealm()
//
//        presenter = UserPresenter(
//            this)
//
//
//        val Name= findViewById<TextView>(R.id.authNameTextView)
//        val Usn= findViewById<TextView>(R.id.authUsernameTextView)
//        val Password = findViewById<TextView>(R.id.authPasswordTextView)
//        val SkinType = findViewById<Spinner>(R.id.spActivity)
//        val buttonSubmit = findViewById<FrameLayout>(R.id.btn_submit)
//        val back = findViewById<ImageView>(R.id.ic_back)
//        val out = findViewById<Button>(R.id.btn_signOut)
//
//        val userModel = presenter.getUser()
//        Log.d("Profile", "Data User: ${userModel?.skinType}")
//
//        if (userModel != null) {
//
//            Name.text = userModel.name
//            Usn.text = userModel.username
//            Password.text = userModel.password
//
//            // Setel posisi item yang dipilih di Spinner berdasarkan nilai skinType
//            userModel.skinType?.let { skinType ->
//                val skinTypeIndex = getIndexOfSkinType(skinType)
//                SkinType.setSelection(skinTypeIndex)
//            }
//        }
//
//
//        buttonSubmit.setOnClickListener {
//            val skinType = SkinType.selectedItem.toString()
//
//            if (skinType.isEmpty()) {
//                Toast.makeText(this, "Choose your skin type", Toast.LENGTH_SHORT).show()
//            } else {
//                userModel?.let { user ->
//                    user.id?.let { userId ->
//                        presenter.editUser(userId, skinType) { isSuccess ->
//                            if (isSuccess) {
//                                // Jika penyuntingan berhasil, tampilkan pesan sukses
//                                Toast.makeText(this, "User profile updated successfully. Please login again", Toast.LENGTH_SHORT).show()
//
//                                // Perbarui UI sesuai dengan jenis kulit yang baru
//                                user.skinType = skinType
//
//                                // Perbarui UI sesuai dengan jenis kulit yang baru
//                                updateUserUI(user)
//                                val skinTypeIndex = getIndexOfSkinType(user.skinType ?: "")
//                                SkinType.setSelection(skinTypeIndex)
//
//                                presenter.logout()
//                                navigateToLogin()
//                                // Perbarui UI atau lakukan tindakan lain yang sesuai dengan perubahan data pengguna
//                            } else {
//                                // Jika penyuntingan gagal, tampilkan pesan kesalahan
//                                Toast.makeText(this, "Failed to update user profile. Please try again.", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        back.setOnClickListener {
//            onBackPressed()
//        }
//        out.setOnClickListener {
//            presenter.logout()
//            checkLoginStatus()
//            navigateToLogin()
//        }
//    }
//
//    private fun checkLoginStatus() {
//        val isLoggedIn = LoginManager.isLoggedIn()
//        Log.d("LoginStatus", "Is user logged in after logout: $isLoggedIn")
//        Toast.makeText(this, "Is user logged in after logout: $isLoggedIn", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun getIndexOfSkinType(skinType: String): Int {
//        return when (skinType) {
//            "Sensitive" -> 0
//            "Normal" -> 1
//            "Oily" -> 2
//            "Dry" -> 3
//            else -> 0 // Default to Sensitive if skin type is unknown
//        }
//    }
//
//    private fun updateUserUI(userModel: UserModel) {
//        // Update UI dengan data pengguna yang diperbarui
//        val Name= findViewById<TextView>(R.id.authNameTextView)
//        val Usn= findViewById<TextView>(R.id.authUsernameTextView)
//        val Password = findViewById<TextView>(R.id.authPasswordTextView)
//        val SkinType = findViewById<Spinner>(R.id.spActivity)
//
//        Name.text = userModel.name
//        Usn.text = userModel.username
//        Password.text = userModel.password
//
//        val skinTypeIndex = getIndexOfSkinType(userModel.skinType ?: "")
//        SkinType.setSelection(skinTypeIndex)
//    }
//
//    private fun navigateToLogin() {
//        val intent = Intent(this, SignInActivity::class.java)
//        startActivity(intent)
//        finish() // Selesaikan activity saat ini agar pengguna tidak dapat kembali ke halaman profil tanpa login ulang
//    }
//
//    override fun displayUser(result: ResultState<UserModel>) {
//        when (result) {
//            is ResultState.Success -> {
//                // Handle data berhasil diterima
//                val userData = result.data
//                updateUserUI(userData)
//            }
//            is ResultState.Error -> {
//                // Handle jika terjadi error
//                val errorMessage = result.error
//                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//            is ResultState.Loading -> {
//                // Handle loading state
//                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//}