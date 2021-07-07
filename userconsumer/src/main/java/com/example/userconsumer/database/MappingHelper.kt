package com.example.userconsumer.database

import android.database.Cursor
import com.example.userconsumer.model.User

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val listFavorite = ArrayList<User>()
        cursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMNS_USERNAME))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMNS_TYPE))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMNS_AVATAR))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMNS_LOCATION))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMNS_COMPANY))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMNS_REPOSITORY))
                listFavorite.add(User(username, type, avatar, location, company, repository))
            }
        }
        return listFavorite
    }
}