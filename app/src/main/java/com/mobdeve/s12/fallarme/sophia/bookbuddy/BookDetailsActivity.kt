package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.Book

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var myDbHelper: MyDbHelper
    private lateinit var book: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_layout)

        // Initialize DB Helper
        myDbHelper = MyDbHelper(this)

        // Retrieve book data from the intent
      //  book = intent.getParcelableExtra<Book>("book")!!

        book = intent.getSerializableExtra("book") as Book


        // Populate the views with book data
        findViewById<EditText>(R.id.bookReviewTv).setText(book.review ?: "")
        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2).setText(book.status)
        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).setText(book.category)

        // Set up save button click listener
        findViewById<Button>(R.id.saveBtn).setOnClickListener {
            saveBookDetails()
        }
    }

    private fun saveBookDetails() {
        val updatedReview = findViewById<EditText>(R.id.bookReviewTv).text.toString()
        val updatedStatus = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2).text.toString()
        val updatedCategory = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).text.toString()

        // Update book object
        val updatedBook = book.copy(
            review = updatedReview,
            status = updatedStatus,
            category = updatedCategory
        )

        // Update book in database
        val result = myDbHelper.updateBook(updatedBook)
        if (result > 0) {
            Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
        } else {
            Toast.makeText(this, "Failed to update book", Toast.LENGTH_SHORT).show()
        }
    }
}
