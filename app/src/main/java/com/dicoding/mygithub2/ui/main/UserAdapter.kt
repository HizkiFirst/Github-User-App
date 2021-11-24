package com.dicoding.mygithub2.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mygithub2.R
import com.dicoding.mygithub2.data.models.User
import com.dicoding.mygithub2.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listUser = ArrayList<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(listUser: ArrayList<User>) {
        this.listUser.clear()
        this.listUser.addAll(listUser)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userBinding = ItemUserBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]

        Glide.with(holder.itemView.context)
            .load(user.avatar_url)
            .apply(RequestOptions().override(55, 55))
            .into(holder.userBinding.imgUserAvatar)

        holder.userBinding.tvUserUsername.text = user.login

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

}