package com.example.weathernowapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.weathernowapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.signuptext.setOnClickListener{

            binding.progressbarlogin.visibility = View.VISIBLE

            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.loginbutton.setOnClickListener{

            binding.progressbarlogin.visibility = View.VISIBLE

            val email = binding.emailbox.text.toString()
            val pass = binding.passwordbox.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity2::class.java)
                        startActivity(intent)
                        finish()

                        binding.progressbarlogin.visibility = View.GONE

                    } else {
                        Toast.makeText(this, "Credentials are wrong or user doesn't exist", Toast.LENGTH_SHORT).show()

                        binding.progressbarlogin.visibility = View.GONE
                    }
                }


            } else {
                Toast.makeText(this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show()


                binding.progressbarlogin.visibility = View.GONE
            }
        }
    }
}