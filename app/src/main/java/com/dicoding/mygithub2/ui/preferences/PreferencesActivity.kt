package com.dicoding.mygithub2.ui.preferences

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mygithub2.R
import com.dicoding.mygithub2.databinding.ActivityPreferencesBinding

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesActivity : AppCompatActivity() {

    private lateinit var prefBinding: ActivityPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        prefBinding = ActivityPreferencesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(prefBinding.root)

        supportActionBar?.title = getString(R.string.pref_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = SettingPreferences.getInstance(dataStore)
        val prefViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )

        prefViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                prefBinding.apply {
                    if (isDarkModeActive) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        switchTheme.isChecked = true
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        switchTheme.isChecked = false
                    }
                }
            })

        prefBinding.apply {
            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                prefViewModel.saveThemeSetting(isChecked)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}