package com.example.githubuserdicodingproject.data.remote

import com.example.githubuserdicodingproject.data.responses.DetailUserResponse
import com.example.githubuserdicodingproject.data.responses.SearchResponse
import com.example.githubuserdicodingproject.data.responses.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<SearchResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<DetailUserResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<UserResponse>>

    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<UserResponse>>
}