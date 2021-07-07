package com.example.submission3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
        var username: String? = null,
        var name: String? = null,
        var avatar: String? = null,
        var company: String? = null,
        var location: String? = null,
        var repository: String? = null,
        var follower: String? = null,
        var following: String? = null,
        var type: String? = null
): Parcelable