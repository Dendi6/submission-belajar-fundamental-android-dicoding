package com.dendi.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseUser {
    const val AUTHORITY = "com.dendi.githubusers"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val PHOTO = "photo"
            const val USERNAME = "username"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}