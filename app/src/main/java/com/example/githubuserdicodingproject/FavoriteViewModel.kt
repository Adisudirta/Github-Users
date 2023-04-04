package com.example.githubuserdicodingproject

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserdicodingproject.database.FavoriteUser
import com.example.githubuserdicodingproject.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUser()
}