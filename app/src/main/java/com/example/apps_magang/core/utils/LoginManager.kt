package com.example.apps_magang.core.utils

import android.content.Context
import android.content.SharedPreferences
import io.realm.Realm
import io.realm.RealmObject

//object LoginManager {
//    private const val PREF_NAME = "login_status"
//    private const val KEY_IS_LOGGED_IN = "isLoggedIn"
//
//    private fun getSharedPreferences(context: Context): SharedPreferences {
//        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//    }
//
//    fun saveLogin(context: Context, isLoggedIn: Boolean) {
//        val editor = getSharedPreferences(context).edit()
//        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
//        editor.apply()
//    }
//
//    fun isLoggedIn(context: Context): Boolean {
//        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
//    }
//
//}

object LoginManager {
    private const val REALM_NAME = "login.realm"

    fun saveLogin(isLoggedIn: Boolean) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            val loginStatus = realm.where(LoginStatus::class.java).findFirst()
            if (loginStatus == null) {
                val newLoginStatus = realm.createObject(LoginStatus::class.java)
                newLoginStatus.isLoggedIn = isLoggedIn
            } else {
                loginStatus.isLoggedIn = isLoggedIn
            }
        }
        realm.close()
    }


    fun isLoggedIn(): Boolean {
        val realm = Realm.getDefaultInstance()
        val loginStatus = realm.where(LoginStatus::class.java).findFirst()
        val isLoggedIn = loginStatus?.isLoggedIn ?: false
        realm.close()
        return isLoggedIn
    }
}

open class LoginStatus(var isLoggedIn: Boolean = false) : RealmObject()