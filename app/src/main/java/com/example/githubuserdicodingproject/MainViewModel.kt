package com.example.githubuserdicodingproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserdicodingproject.data.remote.ApiConfig
import com.example.githubuserdicodingproject.data.responses.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> = _searchResult


    companion object{
        const val TAG = "MainViewModel"
    }

    fun findUsers(query: String) = viewModelScope.launch {
        _isLoading.value = true

        try {
            val response = withContext(Dispatchers.IO) {
                ApiConfig.getApiService().searchUsers(query).execute()
            }

            _isLoading.value = false

            if (response.isSuccessful) {
                val searchResponse = response.body()

                _searchResult.value = searchResponse
            } else {
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        } catch (t: Throwable) {
            _isLoading.value = false
            Log.e(TAG, "onFailure: ${t.message.toString()}")
        }
    }
}