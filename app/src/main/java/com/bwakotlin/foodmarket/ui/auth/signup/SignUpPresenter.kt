package com.bwakotlin.foodmarket.ui.auth.signup

import android.content.Context
import android.net.Uri
import android.view.View
import com.bwakotlin.foodmarket.model.request.RegisterRequest
import com.bwakotlin.foodmarket.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class SignUpPresenter (private val view: SignUpContract.View): SignUpContract.Presenter {
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun submitRegister(registerRequest: RegisterRequest, viewParms: View) {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.register(
            registerRequest.name,
            registerRequest.email,
            registerRequest.password,
            registerRequest.password_confirmation,
            registerRequest.address,
            registerRequest.city,
            registerRequest.houseNumber,
            registerRequest.phoneNumber,
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success", true)) {
                        it.data?.let { it1 -> view.onRegisterSuccess(it1, viewParms) }
                    } else {
                        view.onRegisterFailed(it.meta?.message.toString())
                    }
                },
                { throwable ->
                    view.dismissLoading()
                    if (throwable is HttpException) {
                        val errorBody = throwable.response()?.errorBody()?.string()
                        try {
                            // Mengurai JSON response error
                            val jsonObject = JSONObject(errorBody!!)
                            val errors = jsonObject.getJSONObject("errors")

                            // Menggabungkan semua pesan error menjadi satu string
                            val errorMessages = mutableListOf<String>()
                            errors.keys().forEach { key ->
                                val errorArray = errors.getJSONArray(key)
                                for (i in 0 until errorArray.length()) {
                                    errorMessages.add(errorArray.getString(i))
                                }
                            }

                            val finalErrorMessage = errorMessages.joinToString(" ")
                            view.onRegisterFailed(finalErrorMessage)
                        } catch (e: JSONException) {
                            view.onRegisterFailed("Error parsing response")
                        }
                    } else {
                        view.onRegisterFailed(throwable.message.toString())
                    }
                }
            )
        mCompositeDisposable.add(disposable)
    }

    override fun submitPhotoRegister(filePath: Uri, viewParms: View) {
        view.showLoading()

        val context = viewParms.context
        val profileImageFile = getFileFromUri(context, filePath)
        if (profileImageFile == null) {
            view.onRegisterFailed("Failed to get image file from URI")
            return
        }

        val profileImageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), profileImageFile)
        val profileImageParms = MultipartBody.Part.createFormData("file", profileImageFile.name, profileImageRequestBody)

        val disposable = HttpClient.getInstance().getApi()!!.registerPhoto(profileImageParms)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success", true)) {
                        it.data?.let { _ -> view.onRegisterPhotoSuccess(viewParms) }
                    } else {
                        view.onRegisterFailed(it.meta?.message.toString())
                    }
                },
                { throwable ->
                    view.dismissLoading()
                    if (throwable is HttpException) {
                        val errorBody = throwable.response()?.errorBody()?.string()
                        try {
                            // Mengurai JSON response error
                            val jsonObject = JSONObject(errorBody!!)

                            // Memeriksa apakah respons berisi objek meta dan data
                            if (jsonObject.has("meta") && jsonObject.has("data")) {
                                val fileErrors = jsonObject.getJSONObject("data")
                                    .getJSONObject("error")
                                    .getJSONArray("file")

                                // Menggabungkan semua pesan error file menjadi satu string
                                val fileErrorMessages = mutableListOf<String>()
                                for (i in 0 until fileErrors.length()) {
                                    fileErrorMessages.add(fileErrors.getString(i))
                                }

                                val finalErrorMessage = fileErrorMessages.joinToString(" ")
                                view.onRegisterFailed(finalErrorMessage)
                            } else if (jsonObject.has("message")) {
                                val message = jsonObject.getString("message")
                                view.onRegisterFailed(message)
                            } else {
                                view.onRegisterFailed("Terjadi kesalahan yang tidak diketahui.")
                            }
                        } catch (e: JSONException) {
                            view.onRegisterFailed("Error parsing response")
                        }
                    } else {
                        view.onRegisterFailed(throwable.message.toString())
                    }
                }
            )
        mCompositeDisposable.add(disposable)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream: InputStream?
        try {
            inputStream = context.contentResolver.openInputStream(uri) ?: return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        val tempFile = File(context.cacheDir, "temp_image_file")
        tempFile.outputStream().use { outputStream ->
            copyStream(inputStream, outputStream)
        }
        return tempFile
    }

    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (input.read(buffer).also { read = it } != -1) {
            output.write(buffer, 0, read)
        }
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {
        mCompositeDisposable.clear()
    }
}