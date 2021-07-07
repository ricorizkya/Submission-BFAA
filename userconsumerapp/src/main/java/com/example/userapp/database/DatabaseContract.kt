package com.example.submission3.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTH = "com.example.submission3"
    const val SCHEME = "content"

    class FavoriteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "tb_favorite"
            const val COLUMNS_USERNAME = "username"
            const val COLUMNS_TYPE = "type"
            const val COLUMNS_AVATAR = "avatar_url"
            const val COLUMNS_COMPANY = "company"
            const val COLUMNS_REPOSITORY = "repository"
            const val COLUMNS_LOCATION = "location"
            const val COLUMNS_FAVORITE = "favorite"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTH)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}