package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

//import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.Book

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var myDbHelper: MyDbHelper
    private lateinit var book: Book
    private lateinit var statusSpinner: Spinner
    private lateinit var categorySpinner: Spinner
    private lateinit var bookCoverImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_layout)

        // Initialize DB Helper
        myDbHelper = MyDbHelper(this)

        // Retrieve book data from the intent
        book = intent.getSerializableExtra("book") as Book
        Log.d("BookDetailsActivity", "Received book: ${book.title}")


        // Populate the views with book data
        findViewById<EditText>(R.id.bookReviewTv).setText(book.review ?: "")
//        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2).setText(book.status)
//        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).setText(book.category)

        // Initialize view
        statusSpinner = findViewById(R.id.spinner_status)
        categorySpinner = findViewById(R.id.spinner_category)
        bookCoverImageView = findViewById(R.id.bookCoverImg)

        // Set the book cover image
        Glide.with(this)
            .load(book.image)  // URL or file path to the book cover image
            .into(bookCoverImageView)

        // Set up status spinner
        val statuses = listOf("Currently Reading", "To Read", "Finished")
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = statusAdapter

        // Set current status
        val currentStatusPosition = statuses.indexOf(book.status)
        if (currentStatusPosition >= 0) {
            statusSpinner.setSelection(currentStatusPosition)
        }

        // Retrieve accountId from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val accountId = sharedPreferences.getLong("accountId", -1L)

        // Set up category spinner
        val categories = myDbHelper.getCategoriesByAccountId(accountId)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Set current category
        val currentCategoryPosition = categories.indexOf(book.category)
        if (currentCategoryPosition >= 0) {
            categorySpinner.setSelection(currentCategoryPosition)
        }

        // Set up save button click listener
        findViewById<Button>(R.id.saveBtn).setOnClickListener {
            saveBookDetails()
        }
    }

    private fun saveBookDetails() {
        val updatedReview = findViewById<EditText>(R.id.bookReviewTv).text.toString()
//        val updatedStatus = findViewById<AutoCompleteTextView>(R.id.spinner_status).text.toString()
//        val updatedCategory = findViewById<AutoCompleteTextView>(R.id.spinner_category).text.toString()
        val updatedStatus = statusSpinner.selectedItem.toString()
        val updatedCategory = categorySpinner.selectedItem.toString()

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
