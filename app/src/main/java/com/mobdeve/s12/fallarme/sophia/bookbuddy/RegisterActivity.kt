package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var firstNameEtv: EditText
    private lateinit var lastNameEtv: EditText
    private lateinit var usernameEtv: EditText
    private lateinit var passwordEtv: EditText
    private lateinit var registerBtn: Button
    private lateinit var dbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firstNameEtv = findViewById(R.id.firstNameEtv)
        lastNameEtv = findViewById(R.id.lastNameEtv)
        usernameEtv = findViewById(R.id.usernameEtv)
        passwordEtv = findViewById(R.id.passwordEtv)
        registerBtn = findViewById(R.id.registerBtn)
        dbHelper = MyDbHelper.getInstance(this)!!

        registerBtn.setOnClickListener {
            val firstName = firstNameEtv.text.toString()
            val lastName = lastNameEtv.text.toString()
            val username = usernameEtv.text.toString()
            val password = passwordEtv.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                val newUser = Account(firstName, lastName, username, password)
                val id = dbHelper.insertUser(newUser)

                if (id > 0) {
                    Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
