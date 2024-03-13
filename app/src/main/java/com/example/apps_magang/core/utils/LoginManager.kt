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
////}


object LoginManager {
    private const val PREF_NAME = "login_pref"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}
