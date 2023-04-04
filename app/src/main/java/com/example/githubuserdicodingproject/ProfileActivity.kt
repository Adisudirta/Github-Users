package com.example.githubuserdicodingproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserdicodingproject.adapters.SectionsPagerAdapter
import com.example.githubuserdicodingproject.data.entities.Profile
import com.example.githubuserdicodingproject.database.FavoriteUser
import com.example.githubuserdicodingproject.databinding.ActivityProfileBinding
import com.example.githubuserdicodingproject.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers_text,
            R.string.tab_following_text
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get intent value
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)

        // Assign initial value
        var userInfo: FavoriteUser = FavoriteUser().apply {
            this.avatarUrl = avatarUrl
            this.username = username as String
        }

        // Setup Action Bar
        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        supportActionBar?.title = username as String
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup ViewModel
        profileViewModel = obtainViewModel(this@ProfileActivity)

        profileViewModel.getDetailUser(username)

        profileViewModel.isFavoriteUser.observe(this){
            println(it)
            setButtonIsFavorite(it)
        }

        profileViewModel.isProfileLoading.observe(this){ isProfileLoading ->
            showLoading(isProfileLoading)
        }

        profileViewModel.user.observe(this){user->
            setProfileData(object: Profile{
                override val urlImageProfile: String = user.avatarUrl
                override val name: String = user.name
                override val userName: String = user.login
                override val repositoriesCount: Int = user.publicRepos
                override val followersCount: Int = user.followers
                override val followingCount: Int = user.following
            })
        }

        profileViewModel.getAllFavoriteUser().observe(this){ favoriteUserList ->
            favoriteUserList?.forEach {
                if(it.username == username){
                    profileViewModel.setIsFavorite(true)
                    userInfo = it
                }
            }
        }

        // Setup Following & Followers Tabs
        val bundle = Bundle()
        bundle.putString("username", username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.setBundle(bundle)

        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        // When users click button favorite
        binding.btnFavorite.setOnClickListener {
            profileViewModel.modifyFavorite(userInfo, this)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setProfileData(userData: Profile){
        Glide.with(this)
            .load(userData.urlImageProfile)
            .into(binding.imgDetailProfile)

        binding.tvProfileName.text = userData.name
        binding.tvProfileUserName.text = userData.userName
        binding.tvRepository.text = resources.getString(R.string.repository_text, userData.repositoriesCount)
        binding.tvFollowers.text = resources.getString(R.string.followers_text, userData.followersCount)
        binding.tvFollowing.text = resources.getString(R.string.following_text, userData.followingCount)
    }

    private fun setButtonIsFavorite(isFavorite: Boolean){
        binding.iconFavoriteBorder.visibility = if(!isFavorite) View.VISIBLE else View.GONE
        binding.iconFavorite.visibility = if(isFavorite) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[ProfileViewModel::class.java]
    }
}