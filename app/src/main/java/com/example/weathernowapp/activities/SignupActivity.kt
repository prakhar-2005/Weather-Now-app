package com.example.weathernowapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.weathernowapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.logintxt.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            binding.signupprogressbar.visibility = View.VISIBLE
            startActivity(intent)
            finish()
        }
        binding.signupbutton.setOnClickListener {
            val email = binding.emailbox.text.toString()
            val pass = binding.passwordbox.text.toString()
            binding.signupprogressbar.visibility = View.VISIBLE

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                binding.signupprogressbar.visibility = View.GONE


            } else {
                Toast.makeText(this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
                binding.signupprogressbar.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }
    }
}