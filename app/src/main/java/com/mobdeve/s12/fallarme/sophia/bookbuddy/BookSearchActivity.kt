package com.mobdeve.s12.fallarme.sophia.bookbuddy
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class BookSearchActivity : AppCompatActivity() {
    private val viewModel: BookViewModel by viewModels()
    private lateinit var loggedInAccount: Account
  //  private var accountId: Long = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        val dbHelper = MyDbHelper.getInstance(this)


        val accountId = intent.getLongExtra("account_id", -1)
        if (accountId != -1L) {
            loggedInAccount = dbHelper?.getAccountById(accountId) ?: run {
                Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        } else {
            Toast.makeText(this, "Invalid account ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val searchButton: Button = findViewById(R.id.searchButton)
        val queryEditText: EditText = findViewById(R.id.queryEditText)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val adapter = BookAdapter { book -> showSaveBookDialog(book) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = queryEditText.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchBooks(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.books.observe(this, Observer { books ->
            adapter.submitList(books)
            if (books.isEmpty()) {
                Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Books retrieved successfully", Toast.LENGTH_SHORT).show()
            }
        })
    }

   // private fun fetchAccountFromDatabase(accountId: Long): Account {
   //     val dbHelper = MyDbHelper.getInstance(this)
   //     return dbHelper?.getAccountById(accountId) ?: throw IllegalArgumentException("Account not found")
   // }

    private fun showSaveBookDialog(book: Book) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.popup_save_book, null)
        val statusSpinner: Spinner = dialogView.findViewById(R.id.spinner_status)
        val categorySpinner: Spinner = dialogView.findViewById(R.id.spinner_category)
        val newCategoryEditText: EditText = dialogView.findViewById(R.id.edittext_new_category)
        val saveButton: Button = dialogView.findViewById(R.id.button_save)
        val cancelButton: Button = dialogView.findViewById(R.id.button_cancel)

        // Default category should be "Add New Category"
        val defaultCategory = "Add New Category"

        val categories = loggedInAccount.category.toMutableList().apply { add(defaultCategory) }
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Set the default selection to "Add New Category"
        categorySpinner.setSelection(categories.indexOf(defaultCategory))

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

        val dbHelper = MyDbHelper.getInstance(this)
        val savedBooks = dbHelper?.getBooksByAccountId(loggedInAccount.id) ?: emptyList()

        val isBookSaved = savedBooks.any { it.title == book.title }
        if (isBookSaved) {
            saveButton.isEnabled = false
            saveButton.text = "Already Saved"
          //  saveButton.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.darkergray))
        }

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton(null, null) // No action for default button
            .setNegativeButton(null, null) // No action for default button
            .create()

        // Handle Save button click
        saveButton.setOnClickListener {
            val selectedStatus = statusSpinner.selectedItem.toString()
            val selectedCategory = if (newCategoryEditText.visibility == View.VISIBLE) {
                newCategoryEditText.text.toString().also {
                    categories.add(categories.size - 1, it) // Add the new category to the list
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

        // Handle Cancel button click
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveBookToAccount(account: Account, book: Book) {
        val dbHelper = MyDbHelper.getInstance(this)
        val bookId = dbHelper?.insertBook(book, account.id)

        if (bookId != -1L) {
            account.addBook(bookId.toString()) // Store the book ID as a string

            val updateResult = dbHelper?.updateAccount(account)
            if (updateResult!! > 0) {
                Log.d("BookSearchActivity", "Book saved to account: ${book.title}")
                Log.d("BookSearchActivity", "Saved Books: ${account.savedBooks.joinToString()}")
            } else {
                Log.e("BookSearchActivity", "Failed to update account with saved book")
            }
        } else {
            Log.e("BookSearchActivity", "Failed to save book to the database")
        }
    }

    private fun addCategoryToAccount(account: Account, categoryName: String) {
        account.addCategory(categoryName)

        val dbHelper = MyDbHelper.getInstance(this)
        dbHelper?.updateAccount(account)

        Log.d("BookSearchActivity", "Categories: ${account.category.joinToString(", ")}")
    }
}

/*

class BookSearchActivity : AppCompatActivity() {
    private val viewModel: BookViewModel by viewModels()
    private lateinit var loggedInAccount: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        // Initialize loggedInAccount with the account data, for example, from an intent or shared preferences
        loggedInAccount = Account("John", "Doe", "johndoe", "password123") // Example initialization

        val searchButton: Button = findViewById(R.id.searchButton)
        val queryEditText: EditText = findViewById(R.id.queryEditText)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val adapter = BookAdapter { book -> showSaveBookDialog(book) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = queryEditText.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchBooks(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.books.observe(this, Observer { books ->
            adapter.submitList(books)
            if (books.isEmpty()) {
                Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Books retrieved successfully", Toast.LENGTH_SHORT).show()
            }
        })
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

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
           // .setTitle("Save Book")
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

    /*private fun saveBookToAccount(account: Account, book: Book) {
        val dbHelper = MyDbHelper.getInstance(this)
        val bookId = dbHelper?.insertBook(book, account.id)

        if (bookId != null && bookId != -1L) {
            account.savedBooks.add(book.toString()) // Add the entire book object to savedBooks

            dbHelper.updateAccount(account)

            //Log.d("BookSearchActivity", "Saved Books: ${account.savedBooks.joinToString { it.title.toString() }}")
            Log.d("BookSearchActivity", "Categories: ${account.category.joinToString(", ")}")
        } else {
            Log.e("BookSearchActivity", "Failed to save book to the database")
        }
    }

     */

    private fun saveBookToAccount(account: Account, book: Book) {
        val dbHelper = MyDbHelper.getInstance(this)
        val bookId = dbHelper?.insertBook(book, account.id)

        if (bookId != -1L) {
            account.addBook(bookId.toString()) // Store the book ID as a string

            val updateResult = dbHelper?.updateAccount(account)
            if (updateResult!! > 0) {
                Log.d("BookSearchActivity", "Book saved to account: ${book.title}")
                Log.d("BookSearchActivity", "Saved Books: ${account.savedBooks.joinToString()}")
            } else {
                Log.e("BookSearchActivity", "Failed to update account with saved book")
            }
        } else {
            Log.e("BookSearchActivity", "Failed to save book to the database")
        }
    }

    private fun addCategoryToAccount(account: Account, categoryName: String) {
        //account.category.add(categoryName)
        account.addCategory(categoryName)

        val dbHelper = MyDbHelper.getInstance(this)
        dbHelper?.updateAccount(account)

       // Log.d("BookSearchActivity", "Saved Books: ${account.savedBooks.joinToString { it.title.toString() }}")
        Log.d("BookSearchActivity", "Categories: ${account.category.joinToString(", ")}")
    }
}

 */


/*
class BookSearchActivity : AppCompatActivity() {
    private val viewModel: BookViewModel by viewModels()
    private lateinit var loggedInAccount: Account


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        // Initialize loggedInAccount with the account data, for example, from an intent or shared preferences
        loggedInAccount = Account("John", "Doe", "johndoe", "password123") // Example initialization

        val searchButton: Button = findViewById(R.id.searchButton)
        val queryEditText: EditText = findViewById(R.id.queryEditText)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val adapter = BookAdapter { book -> showSaveBookDialog(book) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = queryEditText.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchBooks(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.books.observe(this, Observer { books ->
            adapter.submitList(books)
            if (books.isEmpty()) {
                Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Books retrieved successfully", Toast.LENGTH_SHORT).show()
            }
        })
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

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setTitle("Save Book")
            .setPositiveButton("Save") { dialog, _ ->
                val selectedStatus = statusSpinner.selectedItem.toString()
                val selectedCategory = if (newCategoryEditText.visibility == View.VISIBLE) {
                    newCategoryEditText.text.toString().also {
                        categories.add(it)
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
        // Insert book into the books table
        val dbHelper = MyDbHelper.getInstance(this)
        dbHelper?.insertBook(book)

        // Add book ID to the account's savedBooks array
        account.addBook(book.id.toString())

        // Update account in the database
        dbHelper?.updateAccount(account)

        Log.d("BookSearchActivity", "Saved Books: ${account.savedBooks.joinToString(", ")}")
        Log.d("BookSearchActivity", "Categories: ${account.category.joinToString(", ")}")
    }

    private fun addCategoryToAccount(account: Account, categoryName: String) {
        // Add category to the account's category array
        account.addCategory(categoryName)

        // Update account in the database
        val dbHelper = MyDbHelper.getInstance(this)
        dbHelper?.updateAccount(account)

        Log.d("BookSearchActivity", "Saved Books: ${account.savedBooks.joinToString(", ")}")
        Log.d("BookSearchActivity", "Categories: ${account.category.joinToString(", ")}")
    }
}

 */



/*

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BookSearchActivity : AppCompatActivity() {
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        val searchButton: Button = findViewById(R.id.searchButton)
        val queryEditText: EditText = findViewById(R.id.queryEditText)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val adapter = BookAdapter { book -> showSaveBookDialog(book) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = queryEditText.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchBooks(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.books.observe(this, Observer { books ->
            adapter.submitList(books)
            if (books.isEmpty()) {
                Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Books retrieved successfully", Toast.LENGTH_SHORT).show()
            }
        })
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

        val dialog = MaterialAlertDialogBuilder(this)
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

                val dbHelper = MyDbHelper(this)
                val bookId = dbHelper.insertBook(bookToSave)
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
class BookSearchActivity : AppCompatActivity() {
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)



        val searchButton: Button = findViewById(R.id.searchButton)
        val queryEditText: EditText = findViewById(R.id.queryEditText)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val adapter = BookAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { book ->
            val intent = Intent(this, BookActivity::class.java)
            // Pass necessary data if needed using intent.putExtra
            startActivity(intent)
        }

        searchButton.setOnClickListener {
            val query = queryEditText.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchBooks(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.books.observe(this, Observer { books ->
            adapter.submitList(books)
            if (books.isEmpty()) {
                Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Books retrieved successfully", Toast.LENGTH_SHORT).show()
            }
        })

        adapter.setOnItemClickListener { book ->
            val intent = Intent(this, BookActivity::class.java)
            // Optionally, pass book data to the BookActivity
            intent.putExtra("bookTitle", book.title)
            intent.putExtra("bookAuthor", book.author)
            intent.putExtra("bookImage", book.image)
            intent.putExtra("bookRating", book.rating)
            startActivity(intent)
        }
    }
}

/*package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookSearchActivity : AppCompatActivity() {
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        val searchButton: Button = findViewById(R.id.searchButton)
        val queryEditText: EditText = findViewById(R.id.queryEditText)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val adapter = BookAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = queryEditText.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchBooks(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.books.observe(this, Observer { books ->
            adapter.submitList(books)
        })
    }
}

 */

 */
