package com.bwakotlin.foodmarket.ui.auth.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bwakotlin.foodmarket.R
import com.bwakotlin.foodmarket.databinding.FragmentSignUpBinding
import com.bwakotlin.foodmarket.model.request.RegisterRequest
import com.bwakotlin.foodmarket.ui.auth.AuthActivity
import com.github.dhaval2404.imagepicker.ImagePicker

class SignUpFragment : Fragment() {
    var filePath: Uri? = null

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initImagePickerLauncher()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(Regex(emailPattern))
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$"
        return password.matches(Regex(passwordPattern))
    }

    private fun initListener() {
        binding.avatar.setOnClickListener {
            ImagePicker.with(this)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }

        binding.btnContinue.setOnClickListener {
            val fullName = binding.fullNameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (fullName.isEmpty()) {
                binding.fullNameInput.error = "Silakan masukkan Nama Anda"
                binding.fullNameInput.requestFocus()
            } else if (email.isEmpty()) {
                binding.emailInput.error = "Silakan masukkan Email Anda"
                binding.emailInput.requestFocus()
            } else if (!isValidEmail(email)) {
                binding.emailInput.error = "Email tidak valid"
                binding.emailInput.requestFocus()
            } else if (password.isEmpty()) {
                binding.passwordInput.error = "Silakan masukkan Password Anda"
                binding.passwordInput.requestFocus()
            }else if (!isValidPassword(password)) {
                binding.passwordInput.error = "Password harus terdiri dari minimal 8 karakter, 1 huruf besar dan 1 angka"
                binding.passwordInput.requestFocus()
            } else {
                val data = RegisterRequest(
                    fullName,
                    email,
                    password,
                    password,
                    "",
                    "",
                    "",
                    "",
                    filePath
                )

                val bundle = Bundle()
                bundle.putParcelable("data", data)

                Navigation.findNavController(it)
                    .navigate(R.id.action_signup_address, bundle)

                (activity as AuthActivity).toolbarSignUpAddress()
            }
        }
    }

    private fun initImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                filePath = result.data?.data

                Glide.with(this)
                    .load(filePath)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.avatar)
            } else if (result.resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(result.data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}