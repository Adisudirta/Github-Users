package com.example.githubuserdicodingproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserdicodingproject.adapters.UsersRecyclerViewAdapter
import com.example.githubuserdicodingproject.data.entities.UserCard
import com.example.githubuserdicodingproject.data.responses.UserResponse

class FollowersFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UsersRecyclerViewAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_followers, container, false)

        progressBar = view.findViewById(R.id.progressBarFollowers)
        recyclerView = view.findViewById(R.id.rvFollowers)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UsersRecyclerViewAdapter(requireActivity(), emptyList())
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ProfileViewModel::class.java]

        viewModel.isFollowersLoading.observe(viewLifecycleOwner){ isFollowersLoading ->
            showLoading(isFollowersLoading)
        }

        viewModel.followers.observe(viewLifecycleOwner){ followers ->
            getFollowersList(followers)
        }

        arguments?.getString("username")?.let { viewModel.getFollowers(it) }

        return view
    }

    private fun showLoading( isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getFollowersList(socialInfo: List<UserResponse>){
        val users = socialInfo.map{
            object: UserCard{
                override val urlImageProfile: String = it.avatarUrl
                override val userName: String = it.login
            }
        }

        adapter = UsersRecyclerViewAdapter(requireContext(), users)
        recyclerView.adapter = adapter
    }
}
