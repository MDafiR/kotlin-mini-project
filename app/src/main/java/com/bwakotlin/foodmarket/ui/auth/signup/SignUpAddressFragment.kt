package com.bwakotlin.foodmarket.ui.auth.signup

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bwakotlin.foodmarket.FoodMarket
import com.bwakotlin.foodmarket.R
import com.bwakotlin.foodmarket.databinding.FragmentSignUpAddressBinding
import com.bwakotlin.foodmarket.model.request.RegisterRequest
import com.bwakotlin.foodmarket.model.response.login.LoginResponse
import com.bwakotlin.foodmarket.ui.auth.AuthActivity
import com.google.gson.Gson

class SignUpAddressFragment : Fragment(), SignUpContract.View {

    private lateinit var  data: RegisterRequest
    lateinit var presenter: SignUpPresenter
    var progressDialog: Dialog? = null

    private var _binding: FragmentSignUpAddressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = SignUpPresenter(this)
        data = arguments?.getParcelable<RegisterRequest>("data")!!

        initListener()
        initView()
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberPattern = "^08[0-9]{8,}$"
        return phoneNumber.matches(Regex(phoneNumberPattern))
    }

    private fun initListener() {
        binding.btnSignUpNow.setOnClickListener {
            val phoneNumber = binding.phoneNumberInput.text.toString()
            val address = binding.addressInput.text.toString()
            val houseNumber = binding.houseNumberInput.text.toString()
            val city = binding.cityInput.text.toString()

            data.let {
                it.address = address
                it.city = city
                it.houseNumber = houseNumber
                it.phoneNumber = phoneNumber
            }

            if (phoneNumber.isEmpty()) {
                binding.phoneNumberInput.error = "Silakan masukkan nomor telepon Anda"
                binding.phoneNumberInput.requestFocus()
            } else if (!isValidPhoneNumber(phoneNumber)) {
                binding.phoneNumberInput.error = "Nomor telepon harus dimulai dengan '08' dan terdiri dari minimal 10 digit angka"
                binding.phoneNumberInput.requestFocus()
            } else if (address.isEmpty()) {
                binding.addressInput.error = "Silakan masukkan alamat tempat tinggal Anda"
                binding.addressInput.requestFocus()
            } else if (houseNumber.isEmpty()) {
                binding.houseNumberInput.error = "Silakan masukkan nomor tempat tinggal Anda"
                binding.houseNumberInput.requestFocus()
            } else if (city.isEmpty()) {
                binding.cityInput.error = "Silakan masukkan kota tempat tinggal Anda"
                binding.cityInput.requestFocus()
            } else {
                presenter.submitRegister(data, it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRegisterSuccess(loginResponse: LoginResponse, view: View) {
        FoodMarket.getApp().setToken(loginResponse.access_token)

        val gson = Gson()
        val json = gson.toJson(loginResponse.user)
        FoodMarket.getApp().setUser(json)

        if (data.filePath == null) {
            Navigation.findNavController(view)
                .navigate(R.id.action_signup_success, null)
            (activity as AuthActivity).toolbarSignUpSuccess()
        } else {
            presenter.submitPhotoRegister(data.filePath!!, view)
        }
    }

    override fun onRegisterPhotoSuccess(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_signup_success, null)
        (activity as AuthActivity).toolbarSignUpSuccess()
    }

    override fun onRegisterFailed(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        Log.v("errorMsg", message)
    }

    private fun initView() {
        progressDialog = Dialog(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }
}