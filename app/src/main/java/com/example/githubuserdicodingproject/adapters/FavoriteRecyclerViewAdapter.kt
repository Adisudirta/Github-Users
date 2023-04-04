package com.example.githubuserdicodingproject.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserdicodingproject.ProfileActivity
import com.example.githubuserdicodingproject.R
import com.example.githubuserdicodingproject.database.FavoriteUser
import com.example.githubuserdicodingproject.helper.FavoriteUserDiffCallback

class FavoriteRecyclerViewAdapter(private val context: Context ): RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder>() {

    private val users = ArrayList<FavoriteUser>()
    fun setListFavorite(listNotes: List<FavoriteUser>) {
        val diffCallback = FavoriteUserDiffCallback(this.users, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.users.clear()
        this.users.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.user_card, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvCardUserName.text = users[position].username
        viewHolder.imgCardProfile.contentDescription = users[position].username

        Glide.with(viewHolder.itemView.context)
            .load(users[position].avatarUrl)
            .into(viewHolder.imgCardProfile)

        viewHolder.btnToDetail.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(ProfileActivity.EXTRA_USERNAME, users[position].username)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = users.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCardUserName: TextView = view.findViewById(R.id.tvCardUserName)
        val imgCardProfile: ImageView = view.findViewById(R.id.imgCardProfile)
        val btnToDetail: Button = view.findViewById(R.id.btnToDetail)
    }
}