package com.example.githubuserdicodingproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.githubuserdicodingproject.adapters.SectionsPagerAdapter
import com.example.githubuserdicodingproject.data.entities.Profile
import com.example.githubuserdicodingproject.databinding.ActivityProfileBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers_text,
            R.string.tab_following_text
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        supportActionBar?.title = username as String
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val profileViewModel by viewModels<ProfileViewModel>()

        profileViewModel.getDetailUser(username)

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

        val bundle = Bundle()
        bundle.putString("username", username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.setBundle(bundle)

        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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
}