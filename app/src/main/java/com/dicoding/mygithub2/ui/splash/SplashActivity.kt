package com.dicoding.mygithub2.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.mygithub2.R
import com.dicoding.mygithub2.ui.main.MainActivity
import com.dicoding.mygithub2.ui.preferences.PreferencesViewModel
import com.dicoding.mygithub2.ui.preferences.PreferencesViewModelFactory
import com.dicoding.mygithub2.ui.preferences.SettingPreferences
import com.dicoding.mygithub2.ui.preferences.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        getPreferences()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashScreenLogo: ImageView = findViewById(R.id.splash_screen_logo)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        splashScreenLogo.startAnimation(slideAnimation)
        val intent = Intent(this, MainActivity::class.java)

        lifecycleScope.launch(Dispatchers.Default) {
            delay(SPLASH_DURATION)
            startActivity(intent)
            finish()
        }
    }

    private fun getPreferences() {
        val pref = SettingPreferences.getInstance(dataStore)
        val prefViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )

        prefViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })
    }

    companion object {
        const val SPLASH_DURATION = 2000L
    }
}