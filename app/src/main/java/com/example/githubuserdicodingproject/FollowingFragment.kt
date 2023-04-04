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
import com.example.githubuserdicodingproject.helper.ViewModelFactory

class FollowingFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UsersRecyclerViewAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_following, container, false)

        progressBar = view.findViewById(R.id.progressBarFollowing)
        recyclerView = view.findViewById(R.id.rvFollowing)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UsersRecyclerViewAdapter(requireActivity(), emptyList())
        recyclerView.adapter = adapter

        val viewModel = obtainViewModel(this)

        viewModel.isFollowingLoading.observe(viewLifecycleOwner){ isFollowingLoading ->
            showLoading(isFollowingLoading)
        }

        viewModel.following.observe(viewLifecycleOwner){ following ->
            getFollowingList(following)
        }

        arguments?.getString("username")?.let { viewModel.getFollowing(it) }

        return view
    }

    private fun obtainViewModel(fragment: Fragment): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory)[ProfileViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getFollowingList(socialInfo: List<UserResponse>){
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