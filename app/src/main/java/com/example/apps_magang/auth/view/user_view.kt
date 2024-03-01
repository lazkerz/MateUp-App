package com.example.apps_magang.auth.view

import com.example.apps_magang.auth.model.UserModel
import com.example.apps_magang.utils.ResultState

interface user_view {

    fun displayUser(result: ResultState<UserModel>)
}