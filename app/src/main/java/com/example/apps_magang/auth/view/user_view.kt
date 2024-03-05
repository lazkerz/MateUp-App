package com.example.apps_magang.auth.view

import com.example.apps_magang.auth.model.database.UserModel
import com.example.apps_magang.core.utils.ResultState

interface user_view {

    fun displayUser(result: ResultState<UserModel>)
}