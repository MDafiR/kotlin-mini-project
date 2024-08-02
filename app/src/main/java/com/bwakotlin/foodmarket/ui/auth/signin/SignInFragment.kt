package com.bwakotlin.foodmarket.ui.auth.signin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bwakotlin.foodmarket.FoodMarket
import com.bwakotlin.foodmarket.R
import com.bwakotlin.foodmarket.databinding.FragmentSignInBinding
import com.bwakotlin.foodmarket.model.response.login.LoginResponse
import com.bwakotlin.foodmarket.ui.MainActivity
import com.bwakotlin.foodmarket.ui.auth.AuthActivity
import com.google.gson.Gson

class SignInFragment : Fragment(), SignInContract.View {

    lateinit var presenter: SignInPresenter
    var progressDialog: Dialog? = null

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = SignInPresenter(this)

        if (!FoodMarket.getApp().getToken().isNullOrEmpty()) {
            val home = Intent(activity, MainActivity::class.java)
            startActivity(home)
            activity?.finishAffinity()
        }

        initView()

        binding.btnSignUp.setOnClickListener {
            val signUp = Intent(activity, AuthActivity::class.java)
            signUp.putExtra("page_request", 2)
            startActivity(signUp)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (email.isEmpty()) {
                binding.emailInput.error = "Silakan masukkan email Anda"
                binding.emailInput.requestFocus()
            } else if (password.isEmpty()) {
                binding.passwordInput.error = "Silakan masukkan password Anda"
                binding.passwordInput.requestFocus()
            } else {
                presenter.submitLogin(email, password)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLoginSuccess(loginResponse: LoginResponse) {
        FoodMarket.getApp().setToken(loginResponse.access_token)

        val gson = Gson()
        val json = gson.toJson(loginResponse.user)
        FoodMarket.getApp().setUser(json)

        val home = Intent(activity, MainActivity::class.java)
        startActivity(home)
        activity?.finishAffinity()
    }

    override fun onLoginFailed(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
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