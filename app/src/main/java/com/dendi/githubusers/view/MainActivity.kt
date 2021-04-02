package com.dendi.githubusers.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dendi.githubusers.R
import com.dendi.githubusers.model.User
import com.dendi.githubusers.adapter.UsersAdapter
import com.dendi.githubusers.databinding.ActivityMainBinding
import com.dendi.githubusers.viewModel.AllUsersViewModel
import com.dendi.githubusers.viewModel.SearchUserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var getUsersModel: AllUsersViewModel
    private lateinit var searchUserModel :SearchUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecyclerList()
        showLoading(true)
        searchView()
        showAllDataUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerList() {
        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        binding.tvUsers.layoutManager = LinearLayoutManager(this)
        binding.tvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                userDataSelected(data)
            }
        })
    }

    private fun userDataSelected(user: User) {
        val intent = Intent(this@MainActivity, DetailUser::class.java)
        intent.putExtra(DetailUser.EXTRA_DATA, user.userName)
        this@MainActivity.startActivity(intent)
    }

    private fun showAllDataUsers(){
        getUsersModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(AllUsersViewModel::class.java)
        getUsersModel.setAllUsers()
        getUsersModel.getAllUsers().observe(this,{listUsers ->
            if (listUsers != null) {
                adapter.setData(listUsers)
                showLoading(false)
            }
        })
    }

    private fun showSearchDataUsers(username:String){
        searchUserModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchUserViewModel::class.java)
        searchUserModel.setSearchUsers(username)
        showLoading(true)
        searchUserModel.getSearchUsers().observe(this,{listSearchUsers->
            if (listSearchUsers != null){
                adapter.setData(listSearchUsers)
                showLoading(false)
            } else {
                Toast.makeText(this, "Username yang kamu cari tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    showSearchDataUsers(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    showAllDataUsers()
                }else {
                    showSearchDataUsers(newText)
                }

                return true
            }
        })
    }
}