package com.example.githubuserdicodingproject.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserdicodingproject.FollowersFragment
import com.example.githubuserdicodingproject.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var bundle: Bundle? = null

    fun setBundle(bundle: Bundle) {
        this.bundle = bundle
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = when (position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }

        fragment.arguments = bundle

        return fragment as Fragment
    }
}