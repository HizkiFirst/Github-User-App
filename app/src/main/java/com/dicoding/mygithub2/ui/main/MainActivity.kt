package com.dicoding.mygithub2.ui.main

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mygithub2.R
import com.dicoding.mygithub2.data.models.User
import com.dicoding.mygithub2.databinding.ActivityMainBinding
import com.dicoding.mygithub2.ui.detail.DetailActivity
import com.dicoding.mygithub2.ui.favorite.FavoriteActivity
import com.dicoding.mygithub2.ui.preferences.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        adapter = UserAdapter()

        searchViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SearchViewModel::class.java)

        mainBinding.apply {
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = adapter
            rvUsers.setVisibility(false)
        }

        searchViewModel.listUsers.observe(this, {
            mainBinding.apply {
                if (it != null) {
                    adapter.setList(it)
                    showSearchStatus(false)
                    rvUsers.setVisibility(true)

                    if (it.isEmpty()) {
                        Glide.with(this@MainActivity)
                            .load(R.drawable.img_user_not_found)
                            .apply(RequestOptions().override(130, 130))
                            .into(imgSearchStatus)

                        tvSearchStatus.text = getString(R.string.search_status_not_found)
                        rvUsers.setVisibility(false)
                        showSearchStatus(true)
                    }
                }
            }
        })

        searchViewModel.isLoading.observe(this, {
            mainBinding.apply {
                progressBar.setVisibility(it)
            }
        })

        searchViewModel.isFailure.observe(this, {
            if (it) {
                mainBinding.apply {
                    Glide.with(this@MainActivity)
                        .load(R.drawable.img_connection_problem)
                        .apply(RequestOptions().override(130, 130))
                        .into(imgSearchStatus)

                    tvSearchStatus.text = getString(R.string.search_status_connection_problem)

                    rvUsers.setVisibility(false)
                    showSearchStatus(it)
                }
            }
        })

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showDetailedUser(data)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu_main, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.maxWidth = 2000
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.preferences -> {
                val goToPreferencesActivity =
                    Intent(this@MainActivity, PreferencesActivity::class.java)
                startActivity(goToPreferencesActivity)
            }
            R.id.favorite_users -> {
                val goToFavoriteActivity =
                    Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(goToFavoriteActivity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchUser(query: String) {
        mainBinding.apply {
            if (query.isEmpty()) return
            progressBar.setVisibility(true)
            showSearchStatus(false)

            searchViewModel.setSearchUsers(query)
        }
    }

    private fun showSearchStatus(showed: Boolean) {
        mainBinding.imgSearchStatus.setVisibility(showed)
        mainBinding.tvSearchStatus.setVisibility(showed)
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