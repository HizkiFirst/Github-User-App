package com.dicoding.mygithub2.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithub2.api.apiconfig.ApiConfig
import com.dicoding.mygithub2.data.models.SearchUserResponse
import com.dicoding.mygithub2.data.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class SearchViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<ArrayList<User>>()
    val listUsers: LiveData<ArrayList<User>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFailure = MutableLiveData<Boolean>()
    val isFailure: LiveData<Boolean> = _isFailure

    fun setSearchUsers(query: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isFailure.value = false
                    _listUsers.postValue(response.body()?.items)
                } else {
                    _isFailure.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isFailure.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "On Failure"
    }
}