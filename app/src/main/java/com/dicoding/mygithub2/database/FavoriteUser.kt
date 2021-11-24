package com.dicoding.mygithub2.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "fav_user")
@Parcelize
data class FavoriteUser(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "avatar_url")
    val avatar_url: String

) : Parcelable