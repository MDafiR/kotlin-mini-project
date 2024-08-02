package com.bwakotlin.foodmarket.ui.auth.signup

import android.net.Uri
import com.bwakotlin.foodmarket.base.BasePresenter
import com.bwakotlin.foodmarket.base.BaseView
import com.bwakotlin.foodmarket.model.request.RegisterRequest
import com.bwakotlin.foodmarket.model.response.login.LoginResponse

interface SignUpContract {
    interface View: BaseView {
        fun onRegisterSuccess(loginResponse: LoginResponse, view:android.view.View)
        fun onRegisterPhotoSuccess(view:android.view.View)
        fun onRegisterFailed(message: String)
    }

    interface Presenter: SignUpContract, BasePresenter {
        fun submitRegister(registerRequest: RegisterRequest, view:android.view.View)
        fun submitPhotoRegister(filePath: Uri, view:android.view.View)
    }
}