package com.bwakotlin.foodmarket.ui.auth.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bwakotlin.foodmarket.R
import com.bwakotlin.foodmarket.databinding.FragmentSignUpBinding
import com.bwakotlin.foodmarket.ui.auth.AuthActivity

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.btnContinue.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_signup_address, null)

            (activity as AuthActivity).toolbarSignUpAddress()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}