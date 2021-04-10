package com.dendi.githubusers.view

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dendi.githubusers.R
import com.dendi.githubusers.adapter.SectionsPagerAdapter
import com.dendi.githubusers.databinding.ActivityDetailUserBinding
import com.dendi.githubusers.db.DatabaseUser
import com.dendi.githubusers.db.UserHelper
import com.dendi.githubusers.model.User
import com.dendi.githubusers.viewModel.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var getUserModel : UserViewModel
    private lateinit var userHelper : UserHelper
    private var isFavorite = false
    private var position: Int = 0
    private var user: User? = null

    companion object{
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val person = intent.getParcelableExtra<User>(EXTRA_DATA) as User

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        showLoading(true)
        showDataUser(person.userName.toString())
        tabLayout(person.userName.toString())

        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.forFavorite -> {
                Log.d("item","favorite diklik")

                val person = intent.getParcelableExtra<User>(EXTRA_DATA) as User

                val intent = Intent()
                intent.putExtra(EXTRA_NOTE, user)
                intent.putExtra(EXTRA_POSITION, position)

                val values = ContentValues()
                values.put(DatabaseUser.UserColumns.PHOTO, person.photo)
                values.put(DatabaseUser.UserColumns.USERNAME, person.userName)
                val result = userHelper.insert(values)
                if (result > 0) {
                    user?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@DetailUser, "Gagal menambah data", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.share -> {
                Log.d("item","share diklik")
            }
        }
    }

    private fun tabLayout(userName: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        sectionsPagerAdapter.username = userName
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.imgPhoto.visibility = View.GONE
            binding.imgLocation.visibility = View.GONE
            binding.imgCompany.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.imgPhoto.visibility = View.VISIBLE
            binding.imgLocation.visibility = View.VISIBLE
            binding.imgCompany.visibility = View.VISIBLE
        }
    }

    private fun showDataUser(username:String){
        getUserModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        getUserModel.setUser(username)
        getUserModel.getUser().observe(this,{userData ->
            if (userData != null) {
                view(userData[0])
                showLoading(false)
            }
        })
    }

    private fun view( user : User){
        user.name?.let { setActionBarTitle(it) }
        Glide.with(this)
                .load(user.photo)
                .apply(RequestOptions().override(150,150))
                .into(binding.imgPhoto)
        binding.tvName.text = user.name
        binding.tvUsername.text = user.userName
        binding.tvLocation.text = user.location
        binding.tvCompany.text = user.company

        val followers = user.followers
        val following = user.following
        val repo = user.repository
        val text = resources.getString(R.string.user_sum,followers,following,repo)
        binding.tvDetailSum.text = text
    }
}