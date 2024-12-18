package com.example.vadisabilitycalc

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vadisabilitycalc.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    // Create a set of bindings
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.registrationSubmit.setOnClickListener{
            val registerUsername = binding.newUsername.text.toString()
            val registerPassword = binding.newPassword.text.toString()
            // the above values are from the text boxes and given a new variable name
            if(registerUsername != "" && registerPassword !== ""){
                registrationToDatabase(registerUsername,registerPassword)
                // These new variables are passed into the function registrationToDatabase
            }else{
                Toast.makeText( this, "You must enter something!", Toast.LENGTH_SHORT).show()
            }

        }

        binding.loginRedirect.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registrationToDatabase(username: String, password: String){
        val insertedRowId = databaseHelper.insertUser(username,password)
        if( insertedRowId != -1L){
            Toast.makeText( this, "Signup Successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Signup Failed!", Toast.LENGTH_SHORT).show()
        }
    }

}