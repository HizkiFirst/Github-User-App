package com.dicoding.mygithub2.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithub2.R
import com.dicoding.mygithub2.data.models.User
import com.dicoding.mygithub2.databinding.FragmentFollowersBinding
import com.dicoding.mygithub2.ui.main.UserAdapter

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _followersBinding: FragmentFollowersBinding? = null
    private val followersBinding get() = _followersBinding
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followersArgs = arguments
        user = followersArgs?.getParcelable<User>(DetailActivity.EXTRA_USER) as User

        _followersBinding = FragmentFollowersBinding.bind(view)

        adapter = UserAdapter()

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)

        followersBinding?.apply {
            rvUsersFollower.setHasFixedSize(true)
            rvUsersFollower.layoutManager = LinearLayoutManager(activity)
            rvUsersFollower.adapter = adapter
            rvUsersFollower.setVisibility(true)
            progressBarFollower.setVisibility(true)
        }

        followersViewModel.setListFollowers(user.login)
        followersViewModel.listFollowers.observe(viewLifecycleOwner, { followers ->
            if (followers != null) {
                adapter.setList(followers)
                followersBinding?.apply {
                    progressBarFollower.setVisibility(false)
                    tvFollowStatus.setVisibility(false)
                    rvUsersFollower.setVisibility(true)
                    if (followers.size == 0) {
                        rvUsersFollower.setVisibility(false)
                        tvFollowStatus.text =
                            getString(R.string.follower_status_no_follower)
                        tvFollowStatus.setVisibility(true)
                    }
                }
            }
        })

        followersViewModel.isFailure.observe(viewLifecycleOwner, {
            if (it) {
                followersBinding?.apply {
                    rvUsersFollower.setVisibility(false)
                    tvFollowStatus.text =
                        getString(R.string.search_status_connection_problem)
                    tvFollowStatus.setVisibility(true)
                    progressBarFollower.setVisibility(false)
                }
            }
        })

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showDetailedUser(data)
            }
        })

    }

    private fun showDetailedUser(data: User) {
        activity?.let {
            val moveToDetailActivity = Intent(it, DetailActivity::class.java)
            moveToDetailActivity.putExtra(DetailActivity.EXTRA_USER, data)
            it.startActivity(moveToDetailActivity)
        }
    }

    private fun View.setVisibility(isVisible: Boolean) {
        if (isVisible) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _followersBinding = null
    }
}