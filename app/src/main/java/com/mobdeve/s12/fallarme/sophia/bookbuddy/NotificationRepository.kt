package com.mobdeve.s12.fallarme.sophia.bookbuddy

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotificationRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun saveNotificationPreferences(userId: String, preferences: NotificationPreferences): Boolean {
        return try {
            usersCollection.document(userId).set(preferences).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun loadNotificationPreferences(userId: String): NotificationPreferences? {
        return try {
            val document = usersCollection.document(userId).get().await()
            document.toObject(NotificationPreferences::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

