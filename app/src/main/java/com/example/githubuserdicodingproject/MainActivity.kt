package com.example.githubuserdicodingproject


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserdicodingproject.adapters.UsersRecyclerViewAdapter
import com.example.githubuserdicodingproject.data.entities.UserCard
import com.example.githubuserdicodingproject.data.responses.UserResponse
import com.example.githubuserdicodingproject.databinding.ActivityMainBinding
import com.example.githubuserdicodingproject.helper.ThemeViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(
            this,
            ThemeViewModelFactory(pref)
        )[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.isLoading.observe(this){isLoading ->
            showLoading(isLoading)
        }

        mainViewModel.searchResult.observe(this){searchResult ->
            binding.tvUsersCount.text = resources.getString(R.string.users_count, searchResult.totalCount)
            setUsersData(searchResult.items)
        }

        binding.searchViewUsername.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                mainViewModel.findUsers(query)

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchViewUsername.windowToken, 0)

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = true
        })

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
    }

    private fun setUsersData(users: List<UserResponse>) {
        val userNames = users.map {
            object : UserCard {
                override val userName: String = it.login
                override val urlImageProfile: String = it.avatarUrl
            }
        }

        binding.rvUsers.adapter = UsersRecyclerViewAdapter(this, userNames)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuFavaorite -> {
                val moveIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.menuTheme -> {
                val moveIntent = Intent(this@MainActivity, ThemeActivity::class.java)
                startActivity(moveIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
