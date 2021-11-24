package com.dicoding.mygithub2.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchUserResponse(
    val items: ArrayList<User>
)

@Parcelize
data class User(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatar_url: String,
) : Parcelable

