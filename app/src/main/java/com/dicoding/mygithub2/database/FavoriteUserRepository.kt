package com.dicoding.mygithub2.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteUserRepository(application: Application) {

    private val mFavoriteUserDao: FavoriteUserDao

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()

    fun addToFavorite(favUser: FavoriteUser) {
        CoroutineScope(Dispatchers.IO).launch {
            mFavoriteUserDao.insert(favUser)
        }
    }

    fun isFavorite(id: Int) = mFavoriteUserDao.isFavorite(id)

    fun removeFromFavorite(favUser: FavoriteUser) {
        CoroutineScope(Dispatchers.IO).launch {
            mFavoriteUserDao.delete(favUser)
        }
    }
}