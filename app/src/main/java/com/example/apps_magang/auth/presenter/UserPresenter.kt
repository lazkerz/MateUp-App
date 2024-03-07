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

    fun getUserById(userId: Int) {
        val realm = Realm.getDefaultInstance()
        val user = realm.where(UserModel::class.java).equalTo("id", userId).findFirst()
        if (user != null) {
            userDataLiveData.postValue(ResultState.Success(user))
        } else {
            userDataLiveData.postValue(ResultState.Error("User not found"))
        }
        realm.close()
    }

    fun getUser(): UserModel? {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(UserModel::class.java).findAll()

        val userModel: UserModel? = if (result.isNotEmpty()) {
            realm.copyFromRealm(result)[0]
        } else {
            null
        }
        if (userModel != null) {
            Log.d("UserModel", "User skinType: ${userModel.skinType}")
            userDataLiveData.postValue(ResultState.Success(userModel))
        } else {
            userDataLiveData.postValue(ResultState.Error("No data in Realm"))
        }

        realm.close()
        return userModel
    }

    fun editUser(id: Int, skinType: String, callback: (Boolean) -> Unit) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({ backgroundRealm ->
            val user = backgroundRealm.where(UserModel::class.java).equalTo("id", id).findFirst()
            user?.skinType = skinType
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

        realm.close()

        callback(isSuccess)
    }

    fun logout() {
        LoginManager.saveLogin(false)
    }

    fun getUserLiveData(): LiveData<ResultState<UserModel>> {
        return userDataLiveData
    }
}
