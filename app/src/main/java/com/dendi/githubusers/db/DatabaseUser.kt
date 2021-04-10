package com.dendi.githubusers.db

import android.provider.BaseColumns

internal class DatabaseUser {
    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val PHOTO = "photo"
            const val USERNAME = "username"
        }
    }
}