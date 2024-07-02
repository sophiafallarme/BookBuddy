
package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R
import com.mobdeve.s12.fallarme.sophia.bookbuddy.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEtv: EditText
    private lateinit var passwordEtv: EditText
    private lateinit var loginBtn: Button
    private lateinit var dbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEtv = findViewById(R.id.usernameEtv)
        passwordEtv = findViewById(R.id.passwordEtv)
        loginBtn = findViewById(R.id.loginBtn)
        dbHelper = MyDbHelper.getInstance(this)!!

        val registerLn = findViewById<TextView>(R.id.registerLn)

        registerLn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            val username = usernameEtv.text.toString()
            val password = passwordEtv.text.toString()

            if (login(username, password)) {
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(username: String, password: String): Boolean {
        val users = dbHelper.getAllUsers()
        for (user in users) {
            if (user.username == username && user.password == password) {
                return true
            }
        }
        return false
    }
}
