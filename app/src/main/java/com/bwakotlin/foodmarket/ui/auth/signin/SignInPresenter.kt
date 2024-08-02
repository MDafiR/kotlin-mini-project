package com.bwakotlin.foodmarket.ui.auth.signin

import com.bwakotlin.foodmarket.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class SignInPresenter (private val view: SignInContract.View): SignInContract.Presenter {
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun submitLogin(email: String, password: String) {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success", true)) {
                        it.data?.let { it1 -> view.onLoginSuccess(it1) }
                    } else {
                        it.meta?.message?.let { it1 -> view.onLoginFailed(it1) }
                    }
                },
                { throwable ->
                    view.dismissLoading()
                    if (throwable is HttpException) {
                        val errorBody = throwable.response()?.errorBody()?.string()
                        try {
                            // Mengurai JSON response error
                            val jsonObject = JSONObject(errorBody!!)
                            val errorMessage = jsonObject.getJSONObject("data").getString("message")
                            if (errorMessage == "Unauthorized") {
                                view.onLoginFailed("Email atau password salah")
                            } else {
                                view.onLoginFailed(errorMessage)
                            }
                        } catch (e: JSONException) {
                            view.onLoginFailed("Error parsing response")
                        }
                    } else {
                        view.onLoginFailed(throwable.message.toString())
                    }
                }
            )
        mCompositeDisposable.add(disposable)
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {
        mCompositeDisposable.clear()
    }

}