package com.mobdeve.s12.fallarme.sophia.bookbuddy

object DbReferences {
    const val DATABASE_VERSION = 5
    const val DATABASE_NAME = "books.db"

    const val TABLE_NAME = "accounts"
    const val COLUMN_NAME_FIRST_NAME = "firstName"
    const val COLUMN_NAME_LAST_NAME = "lastName"
    const val COLUMN_NAME_USER_NAME = "username"
    const val COLUMN_NAME_PASSWORD = "password"
    const val COLUMN_NAME_SAVED_BOOKS = "savedBooks"
    const val COLUMN_NAME_CATEGORY = "category"

    const val BOOK_TABLE_NAME = "books"
    const val COLUMN_NAME_BOOK_TITLE = "title"
    const val COLUMN_NAME_BOOK_AUTHOR = "author"
    const val COLUMN_NAME_BOOK_IMAGE = "image"
    const val COLUMN_NAME_BOOK_STATUS = "status"
    const val COLUMN_NAME_BOOK_CATEGORY = "category"
    const val COLUMN_NAME_RATING = "rating"
    const val COLUMN_NAME_ACCOUNT_ID = "accountId"  // Foreign key to link to accounts table
    const val COLUMN_NAME_REVIEW = "review" // review

    const val _ID = "_id"

    const val CREATE_TABLE_STATEMENT = """
        CREATE TABLE $TABLE_NAME (
            $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME_FIRST_NAME TEXT,
            $COLUMN_NAME_LAST_NAME TEXT,
            $COLUMN_NAME_USER_NAME TEXT,
            $COLUMN_NAME_PASSWORD TEXT,
            $COLUMN_NAME_SAVED_BOOKS TEXT,
            $COLUMN_NAME_CATEGORY TEXT
        )
    """


    const val CREATE_BOOK_TABLE_STATEMENT = """
        CREATE TABLE $BOOK_TABLE_NAME (
            $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME_BOOK_TITLE TEXT,
            $COLUMN_NAME_BOOK_AUTHOR TEXT,
            $COLUMN_NAME_BOOK_IMAGE TEXT, 
            $COLUMN_NAME_BOOK_STATUS TEXT,
            $COLUMN_NAME_BOOK_CATEGORY TEXT,
            $COLUMN_NAME_RATING TEXT,
            $COLUMN_NAME_REVIEW TEXT, 
            $COLUMN_NAME_ACCOUNT_ID INTEGER,
            FOREIGN KEY($COLUMN_NAME_ACCOUNT_ID) REFERENCES $TABLE_NAME($_ID)
        )
    """
}

