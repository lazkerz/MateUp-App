package com.example.apps_magang.auth.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class UserModel : RealmObject () {
    var id: Int = 0
    var name: String? = null
    var username: String? = null
    var password: String? = null
    var skinType: String? = null

//    companion object {
//        private val PASSWORD_REGEX = Regex("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+\$).{6,}\$")
//    }
//
//    fun isPasswordValid(passwordToCheck: String): Boolean {
//        return passwordToCheck.matches(PASSWORD_REGEX)
//    }
}