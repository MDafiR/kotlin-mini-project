package com.bwakotlin.foodmarket.ui.auth.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bwakotlin.foodmarket.R
import com.bwakotlin.foodmarket.databinding.FragmentSignUpAddressBinding
import com.bwakotlin.foodmarket.ui.auth.AuthActivity

class SignUpAddressFragment : Fragment() {

    private var _binding: FragmentSignUpAddressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUpNow.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_signup_success, null)
            (activity as AuthActivity).toolbarSignUpSuccess()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}