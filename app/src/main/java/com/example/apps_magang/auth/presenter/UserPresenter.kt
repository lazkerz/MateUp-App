package com.example.apps_magang.auth.presenter

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.auth.view.user_view
import com.example.apps_magang.core.utils.LoginManager
import com.example.apps_magang.core.utils.ResultState
import io.realm.Realm
import io.realm.kotlin.createObject

class UserPresenter(
    private val context: Context,
    private val view : user_view,
) {
    private val userDataLiveData = MutableLiveData<ResultState<UserModel>>()

    fun addUser(name: String, username: String, password: String, skinType: String, callback: (Boolean) -> Unit) {
        Log.d("AddUser", "Skin Type to be saved: $skinType")

        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({ backgroundRealm ->
            val user = backgroundRealm.createObject<UserModel>()
            user.name = name
            user.username = username
            user.password = password
            user.skinType = skinType
        }, {
            realm.close()
            callback(true)
        }, { error ->
            error.printStackTrace()
            realm.close()
            callback(false)
        })
    }

    fun getUser(): UserModel? {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(UserModel::class.java).findFirst()

        val userModel: UserModel? = if (result != null) {
            realm.copyFromRealm(result)
        } else {
            null
        }

        realm.close()
        return userModel
    }


    fun editUser(id: Int, name: String?, usn: String?, password: String?, skinType: String?, callback: (Boolean) -> Unit) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({ backgroundRealm ->
            val user = backgroundRealm.where(UserModel::class.java).equalTo("id", id).findFirst()
            user?.apply {
                name?.let { this.name = it }
                usn?.let { this.username = it }
                password?.let { this.password = it }
                skinType?.let { this.skinType = it }
            }
        }, {
            realm.close()
            callback(true)
        }, { error ->
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

        if (isSuccess) {
            // Simpan data pengguna ke Realm jika login berhasil
            saveUserToRealm(realm, userModel!!)
        }

        realm.close()

        callback(isSuccess)
    }

    fun saveUserToRealm(realm: Realm, userModel: UserModel) {
        realm.executeTransaction { realm ->
            realm.insertOrUpdate(userModel)
        }
    }



    fun logout() {
        LoginManager.saveLoginStatus(context, false)
    }

    fun getUserLiveData(): LiveData<ResultState<UserModel>> {
        return userDataLiveData
    }
}
