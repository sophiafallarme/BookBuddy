package com.mobdeve.s12.fallarme.sophia.bookbuddy

import org.mindrot.jbcrypt.BCrypt
object PasswordUtils {

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun checkPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }
}