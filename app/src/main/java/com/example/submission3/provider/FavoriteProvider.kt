package com.example.submission3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.submission3.database.DatabaseContract.AUTH
import com.example.submission3.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.submission3.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.example.submission3.database.UserHelper

class FavoriteProvider: ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USERNAME = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: UserHelper

        init {
            uriMatcher.addURI(AUTH, TABLE_NAME, USER)
            uriMatcher.addURI(AUTH, "$TABLE_NAME/#", USERNAME)
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (uriMatcher.match(uri)) {
            USER -> userHelper.queryAll()
            USERNAME -> userHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            uriMatcher.match(uri) -> userHelper.insertDB(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val updated: Int = when (USERNAME) {
            uriMatcher.match(uri) -> userHelper.updateDB(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USERNAME) {
            uriMatcher.match(uri) -> userHelper.deleteDB(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}