package com.dicoding.mygithub2.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mygithub2.R
import com.dicoding.mygithub2.data.models.User
import com.dicoding.mygithub2.database.FavoriteUser
import com.dicoding.mygithub2.databinding.ActivityDetailBinding
import com.dicoding.mygithub2.ui.factory.ViewModelFactory
import com.dicoding.mygithub2.ui.preferences.PreferencesActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var detailUserViewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setActionTitleBar(getString(R.string.default_loading))

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_USER, user)

        detailUserViewModel = obtainViewModel(this@DetailActivity)

        detailUserViewModel.setDetailUser(user.login)
        detailUserViewModel.isFavorite(user.id)
        detailBinding.fabAddFavorite.setVisibility(false)

        detailUserViewModel.detailUser.observe(this, { detail ->
            if (detail != null) {
                setActionTitleBar(detail.login)

                detailBinding.apply {
                    Glide.with(this@DetailActivity)
                        .load(detail.avatar_url)
                        .apply(RequestOptions().override(105, 105))
                        .into(imgUserDetailAvatar)

                    tvUserDetailName.text = detail.name
                    tvUserDetailUsername.text = detail.login
                    tvUserDetailRepository.text = detail.public_repos.toString()
                    tvUserDetailFollowers.text = detail.followers.toString()
                    tvUserDetailFollowing.text = detail.following.toString()

                    val defaultNullValue = getString(R.string.default_null)
                    tvUserDetailLocation.text = detail.location ?: defaultNullValue
                    tvUserDetailCompany.text = detail.company ?: defaultNullValue

                }
            }
        })

        detailUserViewModel.isFailure.observe(this, {
            if (it) {
                Toast.makeText(this, R.string.search_status_connection_problem, Toast.LENGTH_SHORT)
                    .show()
            }
            detailBinding.fabAddFavorite.setVisibility(!it)
        })

        val sectionPagerAdapterDetail = SectionPagerAdapter(this, bundle)
        val viewPagerDetail: ViewPager2 = findViewById(R.id.view_pager_user_detail)
        viewPagerDetail.adapter = sectionPagerAdapterDetail
        val tabsDetail: TabLayout = findViewById(R.id.tabs_user_detail)
        TabLayoutMediator(tabsDetail, viewPagerDetail) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        var isFavoriteUser = false
        detailUserViewModel.isFavorite.observe(this, {
            isFavoriteUser = it
            detailBinding.fabAddFavorite
                .setImageResource(
                    if (it) R.drawable.ic_white_baseline_favorite_24
                    else R.drawable.ic_white_favorite_border_24
                )
        })

        detailBinding.fabAddFavorite.setOnClickListener {
            val favUser = FavoriteUser(user.id, user.login, user.avatar_url)
            isFavoriteUser = !isFavoriteUser

            detailBinding.fabAddFavorite
                .setImageResource(
                    if (isFavoriteUser) R.drawable.ic_white_baseline_favorite_24
                    else R.drawable.ic_white_favorite_border_24
                )
            val snackbarMsg =
                getString(if (isFavoriteUser) SNACKBAR_ADD_FAVORITE else SNACKBAR_REMOVE_FAVORITE)
            Snackbar.make(
                detailBinding.fabAddFavorite,
                "${favUser.login} $snackbarMsg",
                Snackbar.LENGTH_SHORT
            ).show()

            if (isFavoriteUser) {
                detailUserViewModel.addToFavorite(favUser)
            } else {
                detailUserViewModel.removeFromFavorite(favUser)
            }
        }
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
                    Intent(this@DetailActivity, PreferencesActivity::class.java)
                startActivity(goToPreferencesActivity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    private fun setActionTitleBar(title: String) {
        supportActionBar?.title = title
    }

    private fun View.setVisibility(isVisible: Boolean) {
        if (isVisible) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )

        @StringRes
        private const val SNACKBAR_ADD_FAVORITE = R.string.favorite_add
        private const val SNACKBAR_REMOVE_FAVORITE = R.string.favorite_remove
    }
}