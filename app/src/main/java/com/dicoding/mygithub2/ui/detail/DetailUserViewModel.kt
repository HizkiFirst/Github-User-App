package com.dicoding.mygithub2.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithub2.api.apiconfig.ApiConfig
import com.dicoding.mygithub2.data.models.DetailUserResponse
import com.dicoding.mygithub2.database.FavoriteUser
import com.dicoding.mygithub2.database.FavoriteUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {

    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isFailure = MutableLiveData<Boolean>()
    val isFailure: LiveData<Boolean> = _isFailure

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite


    fun setDetailUser(login: String) {

        val detailClient = ApiConfig.getApiService().getUserDetail(login)
        detailClient.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    _isFailure.value = false
                    _detailUser.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isFailure.value = true
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun addToFavorite(favUser: FavoriteUser) {
        mFavoriteUserRepository.addToFavorite(favUser)
    }

    fun isFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val isFav = mFavoriteUserRepository.isFavorite(id) > 0
            withContext(Dispatchers.Main) {
                _isFavorite.value = isFav
            }
        }
    }

    fun removeFromFavorite(favUser: FavoriteUser) {
        mFavoriteUserRepository.removeFromFavorite(favUser)
    }

    companion object {
        private const val TAG = "On Failure"
    }
}