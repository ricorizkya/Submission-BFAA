package com.example.consumerapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_user_favorite"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE =
            "CREATE TABLE $TABLE_NAME" +
                    "(${DatabaseContract.FavoriteColumns.COLUMNS_USERNAME} TEXT PRIMARY KEY NOT NULL," +
                    "${DatabaseContract.FavoriteColumns.COLUMNS_TYPE} TEXT NOT NULL," +
                    "${DatabaseContract.FavoriteColumns.COLUMNS_AVATAR} TEXT NOT NULL," +
                    "${DatabaseContract.FavoriteColumns.COLUMNS_COMPANY} TEXT NOT NULL," +
                    "${DatabaseContract.FavoriteColumns.COLUMNS_REPOSITORY} TEXT NOT NULL," +
                    "${DatabaseContract.FavoriteColumns.COLUMNS_LOCATION} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}