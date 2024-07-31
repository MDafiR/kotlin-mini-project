package com.bwakotlin.foodmarket.ui.auth.signin

import com.bwakotlin.foodmarket.base.BasePresenter
import com.bwakotlin.foodmarket.base.BaseView
import com.bwakotlin.foodmarket.model.response.login.LoginResponse

interface SignInContract {
    interface View: BaseView {
        fun onLoginSuccess(loginResponse: LoginResponse)
        fun onLoginFailed(message: String)
    }

    interface Presenter: SignInContract, BasePresenter {
        fun submitLogin(email: String, password: String)
    }
}