package com.dendi.githubusers.helper

import android.database.Cursor
import com.dendi.githubusers.db.DatabaseUser
import com.dendi.githubusers.model.User

object MappingHelper {
    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<User> {
        val favList  = ArrayList<User>()

        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseUser.UserColumns._ID))
                val photo = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.PHOTO))
                val username = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.USERNAME))
                favList.add(User(id, photo, username))
            }
        }
        return favList
    }
}