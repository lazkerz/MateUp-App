package com.example.apps_magang.auth.presenter

import android.content.Context
import android.text.TextUtils
import com.example.apps_magang.auth.model.UserModel
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.utils.LoginManager
import com.example.apps_magang.utils.ResultState
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.createObject
import java.util.Collections

class UserPresenter(
    private val view : user_view,
    private val context: Context
) {

    fun addUser(name: String, username: String, password: String, callback: (Boolean) -> Unit) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({ backgroundRealm ->
            val user = backgroundRealm.createObject<UserModel>() // Objek baru UserModel akan otomatis mendapatkan ID
            user.name = name
            user.username = username
            user.password = password
        }, {
            // Transaksi berhasil, tutup realm dan panggil callback dengan true
            realm.close()
            callback(true)
        }, { error ->
            // Terjadi kesalahan, cetak stack trace, tutup realm, dan panggil callback dengan false
            error.printStackTrace()
            realm.close()
            callback(false)
        })
    }



    fun getUser(): UserModel? {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(UserModel::class.java).findAll()

        val userModel: UserModel? = if (result.isNotEmpty()) {
            // Ambil satu data UserModel pertama dari hasil query
            realm.copyFromRealm(result)[0]
        } else {
            null
        }
        if (userModel != null) {
            view.displayUser(ResultState.Success(userModel))
        } else {
            view.displayUser(ResultState.Error("No data in Realm"))
        }

        realm.close()
        return userModel
    }


    fun login(username: String, password: String, callback: (Boolean) -> Unit) {
        val realm = Realm.getDefaultInstance()
        val userModel = realm.where(UserModel::class.java)
            .equalTo("username", username)
            .equalTo("password", password)
            .findFirst()

        val isSuccess = userModel != null

        realm.close()

        callback(isSuccess)
    }

    fun logout() {
        // Di sini Anda dapat melakukan proses logout yang diperlukan, seperti menghapus data sesi, menghentikan sesi, membersihkan cache, dll.
        // Misalnya, jika Anda menggunakan shared preferences untuk menyimpan status login:
        LoginManager.saveLogin(context, false)
    }

}