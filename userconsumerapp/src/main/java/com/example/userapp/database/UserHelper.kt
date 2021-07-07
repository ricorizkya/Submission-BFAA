package com.example.submission3.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_USERNAME
import com.example.userconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import java.sql.SQLException
import kotlin.jvm.Throws

class UserHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private var sqLiteDatabase: SQLiteDatabase = databaseHelper.writableDatabase

    companion object {
        private const val TABLE_DATABASE = TABLE_NAME
        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open() {
        sqLiteDatabase = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (sqLiteDatabase.isOpen) {
            sqLiteDatabase.close()
        }
    }

    fun queryAll(): Cursor {
        return sqLiteDatabase.query(
                TABLE_DATABASE,
                null,
                null,
                null,
                null,
                null,
                "$COLUMNS_USERNAME ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return sqLiteDatabase.query(
                TABLE_DATABASE,
                null,
                "$COLUMNS_USERNAME = ?",
                arrayOf(id),
                null,
                null,
                null,
                null
        )
    }

    fun insertDB(values: ContentValues?): Long {
        return sqLiteDatabase.insert(TABLE_DATABASE, null, values)
    }

    fun updateDB(id: String, values: ContentValues?): Int {
        return sqLiteDatabase.update(TABLE_DATABASE, values, "$COLUMNS_USERNAME = ?", arrayOf(id))
    }
    fun deleteDB(id: String): Int {
        return sqLiteDatabase.delete(TABLE_DATABASE, "$COLUMNS_USERNAME = '$id'", null)
    }
}