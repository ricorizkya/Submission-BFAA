package com.example.submission3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite (
    var username: String? = null,
    var follower: String? = null,
    var following: String? = null,
    var repository: String? = null,
    var location: String? = null,
    var company: String? = null,
    var avatar: String? = null,
    var type: String? = null,
    var statusFavorite: String? = null
): Parcelable