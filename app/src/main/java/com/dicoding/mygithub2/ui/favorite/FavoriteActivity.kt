package com.dicoding.mygithub2.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mygithub2.R
import com.dicoding.mygithub2.data.models.User
import com.dicoding.mygithub2.databinding.ActivityFavoriteBinding
import com.dicoding.mygithub2.ui.detail.DetailActivity
import com.dicoding.mygithub2.ui.factory.ViewModelFactory
import com.dicoding.mygithub2.ui.main.UserAdapter
import com.dicoding.mygithub2.ui.preferences.PreferencesActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = (getString(R.string.favorite_title))

        adapter = UserAdapter()

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavoriteUser()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showDetailedUser(data)
            }
        })

        favoriteBinding.apply {
            rvFavorites.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorites.setHasFixedSize(true)
            rvFavorites.adapter = adapter
            rvFavorites.setVisibility(false)
        }

        favoriteViewModel.getAllFavoriteUser().observe(this, {
            if (it != null) {
                val listUser = ArrayList<User>()
                for (user in it) {
                    val data = User(user.id, user.login, user.avatar_url)
                    listUser.add(data)
                }
                adapter.setList(listUser)
                favoriteBinding.apply {
                    showFavoriteStatus(false)
                    rvFavorites.setVisibility(true)

                    if (listUser.isEmpty()) {
                        Glide.with(this@FavoriteActivity)
                            .load(R.drawable.img_favorite_status)
                            .apply(RequestOptions().override(130, 130))
                            .into(imgFavoriteStatus)

                        tvFavoriteStatus.text = getString(R.string.favorite_status_empty)
                        rvFavorites.setVisibility(false)
                        showFavoriteStatus(true)
                    }
                }
            }
        })

        favoriteViewModel.isLoading.observe(this, {
            favoriteBinding.apply {
                progressBar.setVisibility(it)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.preferences -> {
                val goToPreferencesActivity =
                    Intent(this@FavoriteActivity, PreferencesActivity::class.java)
                startActivity(goToPreferencesActivity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showFavoriteStatus(showed: Boolean) {
        favoriteBinding.imgFavoriteStatus.setVisibility(showed)
        favoriteBinding.tvFavoriteStatus.setVisibility(showed)
    }

    private fun showDetailedUser(data: User) {
        val moveToDetailActivity = Intent(this, DetailActivity::class.java)
        moveToDetailActivity.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(moveToDetailActivity)
    }

    private fun View.setVisibility(isVisible: Boolean) {
        if (isVisible) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }
}