package com.example.vadisabilitycalc

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vadisabilitycalc.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.submitData.setOnClickListener{
            val loginUsername = binding.name.text.toString()
            val loginPassword = binding.password.text.toString()
            loginDatabase(loginUsername,loginPassword)
        }
        binding.goToRegistration.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginDatabase(username: String, password: String){
        val userExists = databaseHelper.lookForUser(username,password)
        if(userExists){
            Toast.makeText(this, "login Successfull!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
        }
    }
}