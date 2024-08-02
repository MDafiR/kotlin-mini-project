package com.bwakotlin.foodmarket.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.bwakotlin.foodmarket.R
import com.bwakotlin.foodmarket.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.root.findViewById(R.id.toolbar))

        val pageRequest = intent.getIntExtra("page_request", 0)
        if (pageRequest == 2) {
            toolbarSignUp()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentSignIn, true)
                .build()

            Navigation.findNavController(findViewById(R.id.authHostFragment))
                .navigate(R.id.action_signup, null, navOptions)
        }
    }

    fun toolbarSignUp() {
        val toolbar = binding.root.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Sign Up"
        toolbar.subtitle = "Register and eat"
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_000, null)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun toolbarSignUpAddress() {
        val toolbar = binding.root.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Address"
        toolbar.subtitle = "Make sure it’s valid"
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_000, null)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun toolbarSignUpSuccess() {
        val toolbar = binding.root.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.visibility = View.GONE
    }
}