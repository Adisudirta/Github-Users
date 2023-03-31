package com.example.githubuserdicodingproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserdicodingproject.data.remote.ApiConfig
import com.example.githubuserdicodingproject.data.responses.DetailUserResponse
import com.example.githubuserdicodingproject.data.responses.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel: ViewModel(){

    private val _isProfileLoading = MutableLiveData<Boolean>()
    val isProfileLoading: LiveData<Boolean> = _isProfileLoading

    private val _isFollowersLoading = MutableLiveData<Boolean>()
    val isFollowersLoading: LiveData<Boolean> = _isFollowersLoading

    private val _isFollowingLoading = MutableLiveData<Boolean>()
    val isFollowingLoading: LiveData<Boolean> = _isFollowingLoading

    private val _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> = _user

    private val _followers = MutableLiveData<List<UserResponse>>()
    val followers: LiveData<List<UserResponse>> = _followers

    private val _following = MutableLiveData<List<UserResponse>>()
    val following: LiveData<List<UserResponse>> = _following

    companion object{
        const val TAG = "ProfileViewModel"
    }

    fun getDetailUser(username: String) = viewModelScope.launch {
        _isProfileLoading.value = true

        try {
            val response = withContext(Dispatchers.IO) {
                ApiConfig.getApiService().getUser(username).execute()
            }

            _isProfileLoading.value = false

            if (response.isSuccessful) {
                val userDataResponse = response.body()

                _user.value = userDataResponse
            } else {
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        } catch (t: Throwable) {
            _isProfileLoading.value = false
            Log.e(TAG, "onFailure: ${t.message.toString()}")
        }
    }

    fun getFollowers (username: String) = viewModelScope.launch {
        _isFollowersLoading.value = true

        try {
            val response = withContext(Dispatchers.IO) {
                ApiConfig.getApiService().getFollowers(username).execute()
            }

            _isFollowersLoading.value = false

            if (response.isSuccessful) {
                val followersResponse = response.body()

                _followers.value = followersResponse
            } else {
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        } catch (t: Throwable) {
            _isFollowersLoading.value = false
            Log.e(TAG, "onFailure: ${t.message.toString()}")
        }
    }

    fun getFollowing (username: String) = viewModelScope.launch {
        _isFollowingLoading.value = true

        try {
            val response = withContext(Dispatchers.IO) {
                ApiConfig.getApiService().getFollowing(username).execute()
            }

            _isFollowingLoading.value = false

            if (response.isSuccessful) {
                val followingResponse = response.body()

                _following.value = followingResponse
            } else {
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        } catch (t: Throwable) {
            _isFollowingLoading.value = false
            Log.e(TAG, "onFailure: ${t.message.toString()}")
        }
    }
}