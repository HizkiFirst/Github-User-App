package com.dicoding.mygithub2.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithub2.database.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllFavoriteUser() = mFavoriteUserRepository.getAllFavoriteUser()
}