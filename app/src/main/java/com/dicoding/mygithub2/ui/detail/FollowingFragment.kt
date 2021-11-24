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

class FollowingFragment : Fragment(R.layout.fragment_followers) {

    private var _followersBinding: FragmentFollowersBinding? = null
    private val followersBinding get() = _followersBinding
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followersArgs = arguments
        user = followersArgs?.getParcelable<User>(DetailActivity.EXTRA_USER) as User

        _followersBinding = FragmentFollowersBinding.bind(view)

        adapter = UserAdapter()

        followersBinding?.apply {
            rvUsersFollower.setHasFixedSize(true)
            rvUsersFollower.layoutManager = LinearLayoutManager(activity)
            rvUsersFollower.adapter = adapter
            rvUsersFollower.setVisibility(true)
            progressBarFollower.setVisibility(true)
        }

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        followingViewModel.setListFollowing(user.login)
        followingViewModel.listFollowing.observe(viewLifecycleOwner, { following ->
            if (following != null) {
                adapter.setList(following)
                followersBinding?.apply {
                    progressBarFollower.setVisibility(false)
                    tvFollowStatus.setVisibility(false)
                    rvUsersFollower.setVisibility(true)
                    if (following.size == 0) {
                        tvFollowStatus.text = getString(R.string.following_status_no_following)
                        tvFollowStatus.setVisibility(true)
                        rvUsersFollower.setVisibility(false)
                    }
                }
            }
        })

        followingViewModel.isFailure.observe(viewLifecycleOwner, {
            if (it) {
                followersBinding?.apply {
                    tvFollowStatus.text = getString(R.string.search_status_connection_problem)
                    tvFollowStatus.setVisibility(true)
                    progressBarFollower.setVisibility(false)
                    rvUsersFollower.setVisibility(false)
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