
package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R
import com.mobdeve.s12.fallarme.sophia.bookbuddy.RegisterActivity


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerLn = findViewById<TextView>(R.id.registerLn)


        registerLn.setOnClickListener {
            // Intent to start the RegisterActivity
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        /*
        // Set onClickListener for login Button
        loginBtn.setOnClickListener {
            // Placeholder login logic, replace with actual logic
            val isLoginSuccessful = login()

            if (isLoginSuccessful) {
                // Redirect to HomeActivity (or any other activity hosting HomeFragment)
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish() // Optional: Finish the current activity
            }
        }
    }

    // Placeholder login method, replace with actual login logic
    private fun login(): Boolean {
        val username = usernameEtv.text.toString()
        val password = passwordEtv.text.toString()

        // Add your login logic here
        return username == "user" && password == "password" // Example logic
    }
    */
    }
}
