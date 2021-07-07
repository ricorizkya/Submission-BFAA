package com.example.mainapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mainapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_AVATAR
import com.example.mainapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_COMPANY
import com.example.mainapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_LOCATION
import com.example.mainapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_REPOSITORY
import com.example.mainapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_TYPE
import com.example.mainapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMNS_USERNAME
import com.example.mainapp.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_user_favorite"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE =
                "CREATE TABLE $TABLE_NAME" +
                        "($COLUMNS_USERNAME TEXT PRIMARY KEY NOT NULL," +
                        "$COLUMNS_TYPE TEXT NOT NULL," +
                        "$COLUMNS_AVATAR TEXT NOT NULL," +
                        "$COLUMNS_COMPANY TEXT NOT NULL," +
                        "$COLUMNS_REPOSITORY TEXT NOT NULL," +
                        "$COLUMNS_LOCATION TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}