package com.dicoding.mygithub2.api.apiservice

import com.dicoding.mygithub2.BuildConfig
import com.dicoding.mygithub2.data.models.DetailUserResponse
import com.dicoding.mygithub2.data.models.SearchUserResponse
import com.dicoding.mygithub2.data.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val secretKey = BuildConfig.KEY

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token $secretKey")
    fun getUsers(
        @Query("q") query: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $secretKey")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $secretKey")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $secretKey")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}