package com.bwakotlin.foodmarket.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bwakotlin.foodmarket.databinding.FragmentSignUpSuccessBinding
import com.bwakotlin.foodmarket.ui.MainActivity

class SignUpSuccessFragment : Fragment() {

    private var _binding: FragmentSignUpSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFind.setOnClickListener {
            val home = Intent(activity, MainActivity::class.java)
            startActivity(home)
            activity?.finishAffinity()
        }
    }
}