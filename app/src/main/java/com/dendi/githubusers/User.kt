package com.dendi.githubusers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var photo: Int? = null,
    var userName: String? = null,
    var name: String? = null,
    var loacation: String? = null,
    var company: String? = null,
    var repository: String? = null,
    var followers: String? = null,
    var following: String? = null
): Parcelable
