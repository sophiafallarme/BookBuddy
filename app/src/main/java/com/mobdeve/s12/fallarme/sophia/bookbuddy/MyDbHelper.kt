package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
        db.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DbReferences.DROP_TABLE_STATEMENT)
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
        }

        val id = database.insert(DbReferences.TABLE_NAME, null, values)
        database.close()
        return id
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
            users.add(Account(
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_PASSWORD)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DbReferences._ID))
            ))
        }

        cursor.close()
        database.close()

        return users
    }

    private object DbReferences {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "my_database.db"

        const val TABLE_NAME = "userModel"
        const val _ID = "id"
        const val COLUMN_NAME_FIRST_NAME = "first_name"
        const val COLUMN_NAME_LAST_NAME = "last_name"
        const val COLUMN_NAME_USER_NAME = "user_name"
        const val COLUMN_NAME_PASSWORD = "password"

        const val CREATE_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME_FIRST_NAME TEXT, " +
                    "$COLUMN_NAME_LAST_NAME TEXT, " +
                    "$COLUMN_NAME_USER_NAME TEXT, " +
                    "$COLUMN_NAME_PASSWORD TEXT)"

        const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
