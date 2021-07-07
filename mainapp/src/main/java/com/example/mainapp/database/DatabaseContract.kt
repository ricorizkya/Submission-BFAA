package com.example.mainapp.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTH = "com.example.mainapp"
    const val SCHEME = "content"

    class FavoriteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "tb_user_favorite"
            const val COLUMNS_USERNAME = "username"
            const val COLUMNS_TYPE = "type"
            const val COLUMNS_AVATAR = "avatar_url"
            const val COLUMNS_COMPANY = "company"
            const val COLUMNS_REPOSITORY = "repository"
            const val COLUMNS_LOCATION = "location"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTH)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}