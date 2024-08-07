package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
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
    private lateinit var newCategoryEditText: EditText
    private lateinit var loggedInAccount: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_layout)

        // Initialize DB Helper
        myDbHelper = MyDbHelper(this)

        // Retrieve accountId from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val accountId = sharedPreferences.getLong("accountId", -1L)

        if (accountId != -1L) {
            loggedInAccount = myDbHelper.getAccountById(accountId) ?: run {
                Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        } else {
            Toast.makeText(this, "Invalid account ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Retrieve book data from the intent
        book = intent.getSerializableExtra("book") as Book
        Log.d("BookDetailsActivity", "Received book: ${book.title}")

        // Populate the views with book data
        findViewById<EditText>(R.id.bookReviewTv).setText(book.review)
        findViewById<TextView>(R.id.bookTitleTv).text = book.title // New Code: Set book title
        findViewById<TextView>(R.id.bookAuthorTv).text = book.author // New Code: Set book author


        // Initialize view
        statusSpinner = findViewById(R.id.spinner_status)
        categorySpinner = findViewById(R.id.spinner_category)
        bookCoverImageView = findViewById(R.id.bookCoverImg)
        newCategoryEditText = findViewById(R.id.edittext_new_category)

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

        // Fetch and display categories
        val categories = loggedInAccount.category.toMutableList()
        val defaultCategory = "Add New Category"
        categories.add(defaultCategory)

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Set current category
        val currentCategoryPosition = categories.indexOf(book.category)
        if (currentCategoryPosition >= 0) {
            categorySpinner.setSelection(currentCategoryPosition)
        } else {
            categorySpinner.setSelection(categories.size - 1) // Default to "Add New Category"
        }

        // Show the new category EditText if "Add New Category" is selected
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (categories[position] == defaultCategory) {
                    newCategoryEditText.visibility = View.VISIBLE
                } else {
                    newCategoryEditText.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Set up save button click listener
        findViewById<Button>(R.id.saveBtn).setOnClickListener {
            saveBookDetails(accountId)
        }
    }

    private fun saveBookDetails(accountId: Long) {
        val updatedReview = findViewById<EditText>(R.id.bookReviewTv).text.toString()
        val updatedStatus = statusSpinner.selectedItem.toString()
        val selectedCategory = categorySpinner.selectedItem.toString()
        val updatedCategory = if (newCategoryEditText.visibility == View.VISIBLE) {
            newCategoryEditText.text.toString().also {
                if (it.isNotBlank()) {
                    // Add new category to the account
                    addCategoryToAccount(accountId, it)
                }
            }
        } else {
            selectedCategory
        }

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

        // Send a broadcast indicating a book update
        val intent = Intent("com.mobdeve.s12.fallarme.sophia.bookbuddy.BOOK_UPDATED")
        intent.putExtra("bookId", updatedBook.id) // Pass additional data if needed
        sendBroadcast(intent)

        finish() // Close the activity
    }

    private fun addCategoryToAccount(accountId: Long, newCategory: String) {
        // Get the current categories for the account
        val currentCategories = myDbHelper.getCategoriesByAccountId(accountId).toMutableList()

        // Add the new category if it doesn't already exist
        if (newCategory.isNotBlank() && !currentCategories.contains(newCategory)) {
            currentCategories.add(newCategory)

            // Update the account categories in the database
            myDbHelper.updateAccountCategories(accountId, currentCategories)

            // Update the loggedInAccount object with the new categories
            loggedInAccount = myDbHelper.getAccountById(accountId) ?: loggedInAccount
        }
    }
}
