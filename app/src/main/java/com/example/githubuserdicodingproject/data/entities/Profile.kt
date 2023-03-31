package com.example.githubuserdicodingproject.data.entities

interface Profile {
    val urlImageProfile: String
    val name: String
    val userName: String
    val repositoriesCount: Int
    val followersCount: Int
    val followingCount: Int
}