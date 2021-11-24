package com.dicoding.mygithub2.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithub2.api.apiconfig.ApiConfig
import com.dicoding.mygithub2.data.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val _listFollowers = MutableLiveData<ArrayList<User>>()
    val listFollowers: LiveData<ArrayList<User>> = _listFollowers

    private val _isFailure = MutableLiveData<Boolean>()
    val isFailure: LiveData<Boolean> = _isFailure

    fun setListFollowers(login: String) {
        val detailFollowersClient = ApiConfig.getApiService().getUserFollowers(login)
        detailFollowersClient.enqueue(object : Callback<ArrayList<User>> {

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    _isFailure.value = false
                    _listFollowers.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                _isFailure.value = true
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "On Failure"
    }

}