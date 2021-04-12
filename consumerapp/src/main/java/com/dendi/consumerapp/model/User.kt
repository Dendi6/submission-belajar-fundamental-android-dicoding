package com.dendi.consumerapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int? = null,
    var photo: String? = null,
    var userName: String? = null,
    var htmlUrl: String? = null,
    var name: String? = null,
    var location: String? = null,
    var company: String? = null,
    var repository: Int? = null,
    var followers: Int? = null,
    var following: Int? = null
): Parcelable
