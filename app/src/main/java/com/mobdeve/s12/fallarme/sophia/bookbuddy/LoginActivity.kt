
package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.mindrot.jbcrypt.BCrypt

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

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val user = dbHelper.getUserByUsername(username)

                if (user != null && BCrypt.checkpw(password, user.password)) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()


                    // Pass the account ID to the next activity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("account_id", user.id)
                    startActivity(intent)






                    /*
                    // Directly start BookSearchActivity
                    val intent = Intent(this@LoginActivity, BookSearchActivity::class.java)
                    intent.putExtra("account_id", user.id)
                    startActivity(intent)



                     */



                    Log.d("LoginActivity", "Passing account_id: ${user.id} to BookSearchActivity")

                  //  startActivity(Intent(this@LoginActivity, BookActivity::class.java))

                    logSavedBooks(user) // check for savedbooks
                    finish()
                    // Proceed to next activity or home screen
                } else {
                    Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logSavedBooks(account: Account) {
        // Log the saved books
        Log.d("LoginActivity", "Saved Books: ${account.savedBooks.joinToString(", ")}")
        Log.d("LoginActivity", "Categories: ${account.category.joinToString(", ")}")
    }
}
