package com.example.apps_magang.auth.presenter

import android.content.Context
import android.util.Log
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.core.utils.LoginManager
import com.example.apps_magang.core.utils.ResultState
import io.realm.Realm
import io.realm.kotlin.createObject

class UserPresenter(
    private val view : user_view,
) {

    fun addUser(name: String, username: String, password: String, skinType: String, callback: (Boolean) -> Unit) {
        // Menampilkan nilai skinType sebelum menyimpannya
        Log.d("AddUser", "Skin Type to be saved: $skinType")

        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({ backgroundRealm ->
            val user = backgroundRealm.createObject<UserModel>() // Objek baru UserModel akan otomatis mendapatkan ID
            user.name = name
            user.username = username
            user.password = password
            user.skinType = skinType
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
            Log.d("UserModel", "User skinType: ${userModel.skinType}")
            view.displayUser(ResultState.Success(userModel))
        } else {
            view.displayUser(ResultState.Error("No data in Realm"))
        }

        realm.close()
        return userModel
    }


    fun editUser(id: String, name: String, username: String, password: String, skinType: String, callback: (Boolean) -> Unit) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({ backgroundRealm ->
            val user = backgroundRealm.where(UserModel::class.java).equalTo("id", id).findFirst()
            user?.name = name
            user?.username = username
            user?.password = password
            user?.skinType = skinType
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
        LoginManager.saveLogin(false)
    }

}