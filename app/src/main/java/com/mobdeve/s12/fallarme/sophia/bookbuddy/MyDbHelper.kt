package com.mobdeve.s12.fallarme.sophia.bookbuddy


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/*

val hashedPassword = PasswordUtils.hashPassword(user.password)

val values = ContentValues().apply {
    put(MyDbHelper.DbReferences.COLUMN_NAME_FIRST_NAME, user.firstName)
    put(MyDbHelper.DbReferences.COLUMN_NAME_LAST_NAME, user.lastName)
    put(MyDbHelper.DbReferences.COLUMN_NAME_USER_NAME, user.username)
    put(MyDbHelper.DbReferences.COLUMN_NAME_PASSWORD, hashedPassword)
    put(MyDbHelper.DbReferences.COLUMN_NAME_SAVED_BOOKS, user.savedBooks.joinToString(","))
    put(MyDbHelper.DbReferences.COLUMN_NAME_CATEGORY, user.category.joinToString(","))
}

 */

/*
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

 */

/*

class MyDbHelper(context: Context?) : SQLiteOpenHelper(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION) {

    companion object {
        private var instance: MyDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): MyDbHelper? {
            if (instance == null) {
                instance = MyDbHelper(context.applicationContext)
            }
            return instance
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DbReferences.CREATE_USER_TABLE_STATEMENT)
        db.execSQL(DbReferences.CREATE_BOOK_TABLE_STATEMENT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DbReferences.DROP_USER_TABLE_STATEMENT)
        db.execSQL(DbReferences.DROP_BOOK_TABLE_STATEMENT)
        onCreate(db)
    }

    @Synchronized
    fun insertUser(user: Account): Long {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_FIRST_NAME, user.firstName)
            put(DbReferences.COLUMN_NAME_LAST_NAME, user.lastName)
            put(DbReferences.COLUMN_NAME_USER_NAME, user.username)
            put(DbReferences.COLUMN_NAME_PASSWORD, user.password)
            put(DbReferences.COLUMN_NAME_SAVED_BOOKS, user.savedBooks.joinToString(",")) // Convert list to comma-separated string
            put(DbReferences.COLUMN_NAME_CATEGORY, user.category.joinToString(",")) // Convert list to comma-separated string
        }

        val id = database.insert(DbReferences.TABLE_NAME, null, values)
        database.close()
        return id
    }

    @Synchronized
    fun updateUser(user: Account): Int {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_FIRST_NAME, user.firstName)
            put(DbReferences.COLUMN_NAME_LAST_NAME, user.lastName)
            put(DbReferences.COLUMN_NAME_USER_NAME, user.username)
            put(DbReferences.COLUMN_NAME_PASSWORD, user.password)
            put(DbReferences.COLUMN_NAME_SAVED_BOOKS, user.savedBooks.joinToString(",")) // Convert list to comma-separated string
            put(DbReferences.COLUMN_NAME_CATEGORY, user.category.joinToString(",")) // Convert list to comma-separated string
        }

        val rowsUpdated = database.update(DbReferences.TABLE_NAME, values, "${DbReferences._ID} = ?", arrayOf(user.id.toString()))
        database.close()
        return rowsUpdated
    }

    @Synchronized
    fun getUserByUsername(username: String): Account? {
        val database: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = database.query(
            DbReferences.TABLE_NAME,
            null,
            "${DbReferences.COLUMN_NAME_USER_NAME} = ?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )

        var user: Account? = null

        if (cursor.moveToFirst()) {
            val savedBooksString = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_SAVED_BOOKS)) ?: ""
            val categoryString = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_CATEGORY)) ?: ""

            user = Account(
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_PASSWORD)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DbReferences._ID)),
                ArrayList(savedBooksString.split(",").map { it.trim() }),
                ArrayList(categoryString.split(",").map { it.trim() })
            )
        }

        cursor.close()
        database.close()

        return user
    }

    @Synchronized
    fun insertBook(book: Book): Long {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_BOOK_TITLE, book.title)
            put(DbReferences.COLUMN_BOOK_AUTHOR, book.author)
            put(DbReferences.COLUMN_BOOK_IMAGE, book.image)
            put(DbReferences.COLUMN_BOOK_RATING, book.rating)
            put(DbReferences.COLUMN_BOOK_STATUS, book.status)
            put(DbReferences.COLUMN_BOOK_CATEGORY, book.category)
        }

        val id = database.insert(DbReferences.BOOK_TABLE_NAME, null, values)
        database.close()
        return id
    }

    private object DbReferences {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "bookbuddy.db"

        const val TABLE_NAME = "userModel"
        const val _ID = "id"
        const val COLUMN_NAME_FIRST_NAME = "first_name"
        const val COLUMN_NAME_LAST_NAME = "last_name"
        const val COLUMN_NAME_USER_NAME = "user_name"
        const val COLUMN_NAME_PASSWORD = "password"
        const val COLUMN_NAME_SAVED_BOOKS = "saved_books"
        const val COLUMN_NAME_CATEGORY = "category"

        /*
        const val BOOK_TABLE_NAME = "books"
        const val COLUMN_BOOK_ID = "id"
        const val COLUMN_BOOK_TITLE = "title"
        const val COLUMN_BOOK_AUTHOR = "author"
        const val COLUMN_BOOK_IMAGE = "image"
        const val COLUMN_BOOK_RATING = "rating"
        const val COLUMN_BOOK_STATUS = "status"
        const val COLUMN_BOOK_CATEGORY = "category"


         */
        const val BOOK_TABLE_NAME = "books"
        const val COLUMN_BOOK_ID = "id"
        const val COLUMN_BOOK_TITLE = "title"
        const val COLUMN_BOOK_AUTHOR = "author"
        const val COLUMN_BOOK_IMAGE = "image"
        const val COLUMN_BOOK_RATING = "rating"
        const val COLUMN_BOOK_STATUS = "status"
        const val COLUMN_BOOK_CATEGORY = "category"

        const val CREATE_USER_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME_FIRST_NAME TEXT, " +
                    "$COLUMN_NAME_LAST_NAME TEXT, " +
                    "$COLUMN_NAME_USER_NAME TEXT, " +
                    "$COLUMN_NAME_PASSWORD TEXT, " +
                    "$COLUMN_NAME_SAVED_BOOKS TEXT, " +
                    "$COLUMN_NAME_CATEGORY TEXT)"



        const val CREATE_BOOK_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $BOOK_TABLE_NAME (" +
                    "$COLUMN_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_BOOK_TITLE TEXT, " +
                    "$COLUMN_BOOK_AUTHOR TEXT, " +
                    "$COLUMN_BOOK_IMAGE TEXT, " +
                    "$COLUMN_BOOK_RATING TEXT, " +
                    "$COLUMN_BOOK_STATUS TEXT, " +
                    "$COLUMN_BOOK_CATEGORY TEXT)"

      //  const val DROP_BOOK_TABLE_STATEMENT = "DROP TABLE IF EXISTS $BOOK_TABLE_NAME"

        /*
        const val CREATE_BOOK_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $BOOK_TABLE_NAME (" +
                    "$COLUMN_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_BOOK_TITLE TEXT, " +
                    "$COLUMN_BOOK_AUTHOR TEXT, " +
                    "$COLUMN_BOOK_IMAGE TEXT, " +
                    "$COLUMN_BOOK_RATING TEXT, " +
                    "$COLUMN_BOOK_STATUS TEXT, " +
                    "$COLUMN_BOOK_CATEGORY TEXT)"

         */

        const val DROP_USER_TABLE_STATEMENT = "DROP TABLE IF EXISTS $TABLE_NAME"
        const val DROP_BOOK_TABLE_STATEMENT = "DROP TABLE IF EXISTS $BOOK_TABLE_NAME"


    }
}

 */





/*

class MyDbHelper(context: Context?) : SQLiteOpenHelper(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION) {

    companion object {
        private var instance: MyDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): MyDbHelper? {
            if (instance == null) {
                instance = MyDbHelper(context.applicationContext)
            }
            return instance
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
      //  db.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
       // db.execSQL(DbReferences.CREATE_BOOK_TABLE_STATEMENT)
        Log.d("MyDbHelper", "Creating tables...")
        db.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
        db.execSQL(DbReferences.CREATE_BOOK_TABLE_STATEMENT)
        Log.d("MyDbHelper", "Tables created.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //db.execSQL(DbReferences.DROP_TABLE_STATEMENT)
        //onCreate(db)

        Log.d("MyDbHelper", "Upgrading database from version $oldVersion to $newVersion")
        db.execSQL("DROP TABLE IF EXISTS ${DbReferences.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DbReferences.BOOK_TABLE_NAME}")
        onCreate(db)
    }

    fun insertUser(account: Account): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put("firstName", account.firstName)
            put("lastName", account.lastName)
            put("username", account.username)
            put("password", account.password)
            put("savedBooks", account.savedBooks.joinToString(","))  // Storing as comma-separated string
            put("category", account.category.joinToString(","))      // Storing as comma-separated string
        }

        return db.insert("accounts", null, contentValues)
    }

    /*
    @Synchronized
    fun insertUser(user: Account): Long {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_FIRST_NAME, user.firstName)
            put(DbReferences.COLUMN_NAME_LAST_NAME, user.lastName)
            put(DbReferences.COLUMN_NAME_USER_NAME, user.username)
            put(DbReferences.COLUMN_NAME_PASSWORD, user.password)
            put(DbReferences.COLUMN_NAME_SAVED_BOOKS, user.savedBooks.joinToString(",")) // Convert list to comma-separated string
            put(DbReferences.COLUMN_NAME_CATEGORY, user.category.joinToString(",")) // Convert list to comma-separated string
        }

        val id = database.insert(DbReferences.TABLE_NAME, null, values)
        database.close()
        return id
    }

     */

    @Synchronized
    fun deleteUser(id: Long): Int {
        val database = this.writableDatabase
        val rowsDeleted = database.delete(DbReferences.TABLE_NAME, "${DbReferences._ID} = ?", arrayOf(id.toString()))
        database.close()
        return rowsDeleted
    }

    @Synchronized
    fun updateUser(user: Account): Int {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_FIRST_NAME, user.firstName)
            put(DbReferences.COLUMN_NAME_LAST_NAME, user.lastName)
            put(DbReferences.COLUMN_NAME_USER_NAME, user.username)
            put(DbReferences.COLUMN_NAME_PASSWORD, user.password)
            put(DbReferences.COLUMN_NAME_SAVED_BOOKS, user.savedBooks.joinToString(",")) // Convert list to comma-separated string
            put(DbReferences.COLUMN_NAME_CATEGORY, user.category.joinToString(",")) // Convert list to comma-separated string
        }

        val rowsUpdated = database.update(DbReferences.TABLE_NAME, values, "${DbReferences._ID} = ?", arrayOf(user.id.toString()))
        database.close()
        return rowsUpdated
    }

    @Synchronized
    fun getAllUsers(): ArrayList<Account> {
        val database: SQLiteDatabase = this.readableDatabase

        val cursor: Cursor = database.query(
            DbReferences.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${DbReferences.COLUMN_NAME_LAST_NAME} ASC, ${DbReferences.COLUMN_NAME_FIRST_NAME} ASC",
            null
        )

        val users = ArrayList<Account>()

        while (cursor.moveToNext()) {
            val savedBooksString = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_SAVED_BOOKS)) ?: ""
            val categoryString = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_CATEGORY)) ?: ""

            users.add(Account(
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_PASSWORD)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DbReferences._ID)),
                ArrayList(savedBooksString.split(",").map { it.trim() }),
                ArrayList(categoryString.split(",").map { it.trim() })
            ))
        }

        cursor.close()
        database.close()

        return users
    }

    @Synchronized
    fun updateAccount(account: Account): Int {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_FIRST_NAME, account.firstName)
            put(DbReferences.COLUMN_NAME_LAST_NAME, account.lastName)
            put(DbReferences.COLUMN_NAME_USER_NAME, account.username)
            put(DbReferences.COLUMN_NAME_PASSWORD, account.password)
            put(DbReferences.COLUMN_NAME_SAVED_BOOKS, account.savedBooks.joinToString(","))
            put(DbReferences.COLUMN_NAME_CATEGORY, account.category.joinToString(","))
        }
        val rowsUpdated = database.update(DbReferences.TABLE_NAME, values, "${DbReferences._ID} = ?", arrayOf(account.id.toString()))
        database.close()
        return rowsUpdated
    }

    @Synchronized
    fun getUserByUsername(username: String): Account? {
        val database: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = database.query(
            DbReferences.TABLE_NAME,
            null,
            "${DbReferences.COLUMN_NAME_USER_NAME} = ?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )

        var user: Account? = null

        if (cursor.moveToFirst()) {
            user = Account(
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_PASSWORD)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DbReferences._ID)),
                ArrayList(), // Placeholder for saved books
                ArrayList()  // Placeholder for categories
            )
        }

        cursor.close()
        database.close()

        return user
    }

    @Synchronized
    fun insertBook(book: Book): Long {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_BOOK_TITLE, book.title)
            put(DbReferences.COLUMN_BOOK_AUTHOR, book.author)
            put(DbReferences.COLUMN_BOOK_IMAGE, book.image)
            put(DbReferences.COLUMN_BOOK_RATING, book.rating)
            put(DbReferences.COLUMN_BOOK_STATUS, book.status)
            put(DbReferences.COLUMN_BOOK_CATEGORY, book.category)
        }

        //val id = database.insert(DbReferences.BOOK_TABLE_NAME, null, values)
        //database.close()
        //return id

        return try {
            val id = database.insert(DbReferences.BOOK_TABLE_NAME, null, values)
            Log.d("MyDbHelper", "Book inserted with ID: $id")
            id
        } catch (e: Exception) {
            Log.e("MyDbHelper", "Error inserting book", e)
            -1
        } finally {
            database.close()
        }
    }


    private object DbReferences {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "my_database.db"

        const val TABLE_NAME = "userModel"
        const val _ID = "id"
        const val COLUMN_NAME_FIRST_NAME = "first_name"
        const val COLUMN_NAME_LAST_NAME = "last_name"
        const val COLUMN_NAME_USER_NAME = "user_name"
        const val COLUMN_NAME_PASSWORD = "password"
        const val COLUMN_NAME_SAVED_BOOKS = "saved_books"
        const val COLUMN_NAME_CATEGORY = "category"

        // FOR BOOKS
        const val BOOK_TABLE_NAME = "books"
        const val COLUMN_BOOK_ID = "id"
        const val COLUMN_BOOK_TITLE = "title"
        const val COLUMN_BOOK_AUTHOR = "author"
        const val COLUMN_BOOK_IMAGE = "image"
        const val COLUMN_BOOK_RATING = "rating"
        const val COLUMN_BOOK_STATUS = "status"
        const val COLUMN_BOOK_CATEGORY = "category"

        const val CREATE_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME_FIRST_NAME TEXT, " +
                    "$COLUMN_NAME_LAST_NAME TEXT, " +
                    "$COLUMN_NAME_USER_NAME TEXT, " +
                    "$COLUMN_NAME_PASSWORD TEXT, " +
                    "$COLUMN_NAME_SAVED_BOOKS TEXT, " +
                    "$COLUMN_NAME_CATEGORY TEXT)"


        // TABLE FOR BOOKS
        const val CREATE_BOOK_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $BOOK_TABLE_NAME (" +
                    "$COLUMN_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_BOOK_TITLE TEXT, " +
                    "$COLUMN_BOOK_AUTHOR TEXT, " +
                    "$COLUMN_BOOK_IMAGE TEXT, " +
                    "$COLUMN_BOOK_RATING TEXT, " +
                    "$COLUMN_BOOK_STATUS TEXT, " +
                    "$COLUMN_BOOK_CATEGORY TEXT)"

        const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS $TABLE_NAME"
    }


}

 */

class MyDbHelper(context: Context?) : SQLiteOpenHelper(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION) {

    companion object {
        private var instance: MyDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): MyDbHelper? {
            if (instance == null) {
                instance = MyDbHelper(context.applicationContext)
            }
            return instance
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("MyDbHelper", "Creating tables...")
        db.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
        db.execSQL(DbReferences.CREATE_BOOK_TABLE_STATEMENT)
        Log.d("MyDbHelper", "Tables created.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("MyDbHelper", "Upgrading database from version $oldVersion to $newVersion")
        db.execSQL("DROP TABLE IF EXISTS ${DbReferences.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DbReferences.BOOK_TABLE_NAME}")
        onCreate(db)

      /*  if (oldVersion < 2) {
            // Alter the books table to add the new column
            db.execSQL("ALTER TABLE ${DbReferences.BOOK_TABLE_NAME} ADD COLUMN ${DbReferences.COLUMN_NAME_BOOK_IMAGE} TEXT")
        }

       */
    }

    @Synchronized
    fun insertUser(account: Account): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_FIRST_NAME, account.firstName)
            put(DbReferences.COLUMN_NAME_LAST_NAME, account.lastName)
            put(DbReferences.COLUMN_NAME_USER_NAME, account.username)
            put(DbReferences.COLUMN_NAME_PASSWORD, account.password)
            put(DbReferences.COLUMN_NAME_SAVED_BOOKS, account.savedBooks.joinToString(","))
            put(DbReferences.COLUMN_NAME_CATEGORY, account.category.joinToString(","))
        }

        return db.insert(DbReferences.TABLE_NAME, null, contentValues)
    }

    @Synchronized
    fun deleteUser(id: Long): Int {
        val database = this.writableDatabase
        val rowsDeleted = database.delete(DbReferences.TABLE_NAME, "${DbReferences._ID} = ?", arrayOf(id.toString()))
        database.close()
        return rowsDeleted
    }

    @Synchronized
    fun updateUser(user: Account): Int {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_FIRST_NAME, user.firstName)
            put(DbReferences.COLUMN_NAME_LAST_NAME, user.lastName)
            put(DbReferences.COLUMN_NAME_USER_NAME, user.username)
            put(DbReferences.COLUMN_NAME_PASSWORD, user.password)
            put(DbReferences.COLUMN_NAME_SAVED_BOOKS, user.savedBooks.joinToString(","))
            put(DbReferences.COLUMN_NAME_CATEGORY, user.category.joinToString(","))
        }

        val rowsUpdated = database.update(DbReferences.TABLE_NAME, values, "${DbReferences._ID} = ?", arrayOf(user.id.toString()))
        database.close()
        return rowsUpdated
    }

    @Synchronized
    fun getAllUsers(): ArrayList<Account> {
        val database: SQLiteDatabase = this.readableDatabase

        val cursor: Cursor = database.query(
            DbReferences.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${DbReferences.COLUMN_NAME_LAST_NAME} ASC, ${DbReferences.COLUMN_NAME_FIRST_NAME} ASC",
            null
        )

        val users = ArrayList<Account>()

        while (cursor.moveToNext()) {
            val savedBooksString = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_SAVED_BOOKS)) ?: ""
            val categoryString = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_CATEGORY)) ?: ""

            users.add(Account(
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_PASSWORD)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DbReferences._ID)),
                ArrayList(savedBooksString.split(",").map { it.trim() }),
                ArrayList(categoryString.split(",").map { it.trim() })
            ))
        }

        cursor.close()
        database.close()

        return users
    }

    @Synchronized
    fun updateAccount(account: Account): Int {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_FIRST_NAME, account.firstName)
            put(DbReferences.COLUMN_NAME_LAST_NAME, account.lastName)
            put(DbReferences.COLUMN_NAME_USER_NAME, account.username)
            put(DbReferences.COLUMN_NAME_PASSWORD, account.password)
            put(DbReferences.COLUMN_NAME_SAVED_BOOKS, account.savedBooks.joinToString(","))
            put(DbReferences.COLUMN_NAME_CATEGORY, account.category.joinToString(","))
        }
        val rowsUpdated = database.update(DbReferences.TABLE_NAME, values, "${DbReferences._ID} = ?", arrayOf(account.id.toString()))
        database.close()
        return rowsUpdated
    }

    @Synchronized
   /* fun insertBook(book: Book, accountId: Long): Long {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_BOOK_TITLE, book.title)
            put(DbReferences.COLUMN_NAME_BOOK_AUTHOR, book.author)
            put(DbReferences.COLUMN_NAME_BOOK_STATUS, book.status)
            put(DbReferences.COLUMN_NAME_BOOK_CATEGORY, book.category)
            put(DbReferences.COLUMN_NAME_ACCOUNT_ID, accountId)
        }

        return database.insert(DbReferences.BOOK_TABLE_NAME, null, values)
    }


    */
    fun insertBook(book: Book, accountId: Long): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put("title", book.title)
            put("author", book.author)
            put("image", book.image)
            put("rating", book.rating)
            put("status", book.status)
            put("category", book.category)
            put("accountId", accountId)
        }

        return db.insert(DbReferences.BOOK_TABLE_NAME, null, contentValues)
    }


    @Synchronized
    fun getBooksByAccountId(accountId: Long, status: String? = null): List<Book> {
        val database = this.readableDatabase
        val selectionArgs = mutableListOf<String>().apply {
            add(accountId.toString())
            status?.let { add(it) }
        }.toTypedArray()
        val selection = "${DbReferences.COLUMN_NAME_ACCOUNT_ID} = ?" +
                (status?.let { " AND ${DbReferences.COLUMN_NAME_BOOK_STATUS} = ?" } ?: "")

        val cursor = database.query(
            DbReferences.BOOK_TABLE_NAME,
            null,
//            "${DbReferences.COLUMN_NAME_ACCOUNT_ID} = ?" + (status?.let { " AND ${DbReferences.COLUMN_NAME_BOOK_STATUS} = ?" } ?: ""),
//            arrayOf(accountId.toString()),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val books = mutableListOf<Book>()

//        while (cursor.moveToNext()) {
//            books.add(Book(
//                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_TITLE)),
//                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_AUTHOR)),
//                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_STATUS)),
//                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_CATEGORY)),
//                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_RATING)),
//        }

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences._ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_TITLE))
                val author = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_AUTHOR))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_IMAGE))
                val rating = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_RATING))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_STATUS))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_CATEGORY))
                val review = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_REVIEW)   ) ?: ""
                    val retrievedAccountId = cursor.getLong(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_ACCOUNT_ID))
               // val retrievedAccountId = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_ACCOUNT_ID))

                val book = Book(id, title, author, image, rating, status, category, review, retrievedAccountId)
                books.add(book)
            } while (cursor.moveToNext())
        }

        cursor.close()
        database.close()

        return books
    }

    fun getCategoriesByAccountId(accountId: Long): List<String> {
        val categories = mutableListOf<String>()
        val database = this.readableDatabase
        val cursor = database.query(
            true,  // distinct
            DbReferences.BOOK_TABLE_NAME,
            arrayOf(DbReferences.COLUMN_NAME_BOOK_CATEGORY),
            "${DbReferences.COLUMN_NAME_ACCOUNT_ID} = ?",
            arrayOf(accountId.toString()),
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val category = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_BOOK_CATEGORY))
                if (!category.isNullOrEmpty()) {
                    categories.add(category)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        database.close()

        return categories
    }


    fun getUserByUsername(username: String): Account? {
        val db = this.readableDatabase
        val cursor = db.query(
            DbReferences.TABLE_NAME,
            null,
            "${DbReferences.COLUMN_NAME_USER_NAME} = ?",
            arrayOf(username),
            null,
            null,
            null
        )

        var account: Account? = null

        if (cursor.moveToFirst()) {
            val savedBooksString = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_SAVED_BOOKS)) ?: ""
            val categoryString = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_CATEGORY)) ?: ""

            account = Account(
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_PASSWORD)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DbReferences._ID)),
                ArrayList(savedBooksString.split(",").map { it.trim() }),
                ArrayList(categoryString.split(",").map { it.trim() })
            )
        }

        cursor.close()
        db.close()

        return account
    }

    fun getAccountById(id: Long): Account? {
        val db = readableDatabase
        val cursor = db.query(
            DbReferences.TABLE_NAME,
            null,
            "${DbReferences._ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val firstName = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_USER_NAME))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_PASSWORD))
            val savedBooks = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_SAVED_BOOKS)).split(",").toMutableList()
            val category = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_CATEGORY)).split(",").toMutableList()

            Account(firstName, lastName, username, password, id, savedBooks, category)
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    fun updateBook(book: com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.Book): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(DbReferences.COLUMN_NAME_BOOK_TITLE, book.title)
            put(DbReferences.COLUMN_NAME_BOOK_AUTHOR, book.author)
            put(DbReferences.COLUMN_NAME_BOOK_IMAGE, book.image)
            put(DbReferences.COLUMN_NAME_BOOK_STATUS, book.status)
            put(DbReferences.COLUMN_NAME_BOOK_CATEGORY, book.category)
            put(DbReferences.COLUMN_NAME_RATING, book.rating)
            put("review", book.review) // Assuming you have a column for review
        }

        return db.update(
            DbReferences.BOOK_TABLE_NAME,
            contentValues,
            "${DbReferences._ID} = ?",
            arrayOf(book.id)
        )
    }




}
