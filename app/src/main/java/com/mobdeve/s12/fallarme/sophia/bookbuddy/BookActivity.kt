package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BookActivity : AppCompatActivity() {
    private lateinit var loggedInAccount: Account
    private var accountId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        accountId = intent.getLongExtra("account_id", -1)
        if (accountId == -1L) {
            Toast.makeText(this, "Account ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Log.d("BookActivity", "Received account_id: $accountId")

        val bookRecyclerView: RecyclerView = findViewById(R.id.rv)
        val bookAdapter = BookAdapter { book -> showSaveBookDialog(book) }
        bookRecyclerView.adapter = bookAdapter
        bookRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load books data and submit to the adapter
        // bookAdapter.submitList(books)
    }

    private fun fetchAccountFromDatabase(accountId: Long): Account {
        val dbHelper = MyDbHelper.getInstance(this)
        return dbHelper?.getAccountById(accountId) ?: throw IllegalArgumentException("Account not found")
    }

    private fun showSaveBookDialog(book: Book) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.popup_save_book, null)
        val statusSpinner: Spinner = dialogView.findViewById(R.id.spinner_status)
        val categorySpinner: Spinner = dialogView.findViewById(R.id.spinner_category)
        val newCategoryEditText: EditText = dialogView.findViewById(R.id.edittext_new_category)

        val categories = loggedInAccount.category.toMutableList().apply { add("Add New Category") }
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (categories[position] == "Add New Category") {
                    newCategoryEditText.visibility = View.VISIBLE
                } else {
                    newCategoryEditText.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val selectedStatus = statusSpinner.selectedItem.toString()
                val selectedCategory = if (newCategoryEditText.visibility == View.VISIBLE) {
                    newCategoryEditText.text.toString().also {
                        categories.add(categories.size - 1, it)
                        categoryAdapter.notifyDataSetChanged()
                        addCategoryToAccount(loggedInAccount, it) // Add new category to account
                    }
                } else {
                    categorySpinner.selectedItem.toString()
                }

                val bookToSave = book.copy(
                    status = selectedStatus,
                    category = selectedCategory
                )

                saveBookToAccount(loggedInAccount, bookToSave)

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun saveBookToAccount(account: Account, book: Book) {
        val dbHelper = MyDbHelper.getInstance(this)
        val bookId = dbHelper?.insertBook(book, account.id)

        if (bookId != -1L) {
            account.addBook(bookId.toString()) // Store the book ID as a string

            val updateResult = dbHelper?.updateAccount(account)
            if (updateResult!! > 0) {
                Log.d("BookActivity", "Book saved to account: ${book.title}")
                Log.d("BookActivity", "Saved Books: ${account.savedBooks.joinToString()}")
            } else {
                Log.e("BookActivity", "Failed to update account with saved book")
            }
        } else {
            Log.e("BookActivity", "Failed to save book to the database")
        }
    }

    private fun addCategoryToAccount(account: Account, categoryName: String) {
        account.addCategory(categoryName)

        val dbHelper = MyDbHelper.getInstance(this)
        dbHelper?.updateAccount(account)

        Log.d("BookActivity", "Categories: ${account.category.joinToString(", ")}")
    }
}

/*

class BookActivity : AppCompatActivity() {
    private lateinit var loggedInAccount: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        // Initialize loggedInAccount with the account data, for example, from an intent or shared preferences
        loggedInAccount = Account("John", "Doe", "johndoe", "password123") // Example initialization

        val bookRecyclerView: RecyclerView = findViewById(R.id.rv)
        val bookAdapter = BookAdapter { book -> showSaveBookDialog(book) }
        bookRecyclerView.adapter = bookAdapter
        bookRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load books data and submit to the adapter
        // bookAdapter.submitList(books)
    }

    private fun showSaveBookDialog(book: Book) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.popup_save_book, null)
        val statusSpinner: Spinner = dialogView.findViewById(R.id.spinner_status)
        val categorySpinner: Spinner = dialogView.findViewById(R.id.spinner_category)
        val newCategoryEditText: EditText = dialogView.findViewById(R.id.edittext_new_category)

        val categories = resources.getStringArray(R.array.category_array).toMutableList()
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (categories[position] == "Add New Category") {
                    newCategoryEditText.visibility = View.VISIBLE
                } else {
                    newCategoryEditText.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Save Book")
            .setPositiveButton("Save") { dialog, _ ->
                val selectedStatus = statusSpinner.selectedItem.toString()
                val selectedCategory = if (newCategoryEditText.visibility == View.VISIBLE) {
                    newCategoryEditText.text.toString().also {
                        categories.add(it)
                        categoryAdapter.notifyDataSetChanged()
                    }
                } else {
                    categorySpinner.selectedItem.toString()
                }

                val bookToSave = book.copy(
                    status = selectedStatus,
                    category = selectedCategory
                )

                val dbHelper = MyDbHelper.getInstance(this)
                val bookId = dbHelper?.insertBook(bookToSave, loggedInAccount.id)
                if (bookId != -1L) {
                    Toast.makeText(this, "Book saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error saving book", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
}

 */

/*
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class BookActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books) // Ensure the correct layout is used

        val bookTitle = intent.getStringExtra("bookTitle")
        val bookAuthor = intent.getStringExtra("bookAuthor")
        val bookImage = intent.getStringExtra("bookImage")
        val bookRating = intent.getStringExtra("bookRating")

        val saveButton: LinearLayout = findViewById(R.id.saveBookBtn)
        Log.d("BookActivity", "Save button found") // Logging to check if the button is found

        saveButton.setOnClickListener {
            Log.d("BookActivity", "Save button clicked") // Logging to check if the click is detected
            showSaveBookDialog()
        }
    }

    private fun showSaveBookDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.popup_save_book, null)
        val statusSpinner: Spinner = dialogView.findViewById(R.id.spinner_status)
        val categorySpinner: Spinner = dialogView.findViewById(R.id.spinner_category)
        val newCategoryEditText: EditText = dialogView.findViewById(R.id.edittext_new_category)

        val categories = resources.getStringArray(R.array.category_array).toMutableList()
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (categories[position] == "Add New Category") {
                    newCategoryEditText.visibility = View.VISIBLE
                } else {
                    newCategoryEditText.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Save Book")
            .setPositiveButton("Save") { dialog, _ ->
                val selectedStatus = statusSpinner.selectedItem.toString()
                val selectedCategory = if (newCategoryEditText.visibility == View.VISIBLE) {
                    newCategoryEditText.text.toString().also {
                        categories.add(it)
                        categoryAdapter.notifyDataSetChanged()
                    }
                } else {
                    categorySpinner.selectedItem.toString()
                }

                val book = Book(
                    id = "1",
                    title = "Sample Title",  // Replace with actual book title
                    author = "Sample Author",  // Replace with actual book author
                    image = "Sample Image URL",  // Replace with actual book image URL
                    rating = "5.0",  // Replace with actual book rating
                    status = selectedStatus,
                    category = selectedCategory
                )

                val dbHelper = MyDbHelper(this)
                val bookId = dbHelper.insertBook(book)
                if (bookId != -1L) {
                    Toast.makeText(this, "Book saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error saving book", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
}

 */
