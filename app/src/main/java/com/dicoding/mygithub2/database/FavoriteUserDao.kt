package com.dicoding.mygithub2.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {

    @Insert
    fun insert(favUser: FavoriteUser)

    @Delete
    fun delete(favUser: FavoriteUser)

    @Query("SELECT count(*) FROM fav_user WHERE fav_user.id = :id")
    fun isFavorite(id: Int): Int

    @Query("SELECT * FROM fav_user ORDER BY login ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>
}