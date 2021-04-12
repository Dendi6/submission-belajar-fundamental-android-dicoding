package com.dendi.githubusers.view

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dendi.githubusers.R
import com.dendi.githubusers.adapter.UsersFavoriteAdapter
import com.dendi.githubusers.databinding.ActivityFavoriteBinding
import com.dendi.githubusers.db.DatabaseUser.UserColumns.Companion.CONTENT_URI
import com.dendi.githubusers.helper.MappingHelper
import com.dendi.githubusers.model.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: UsersFavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title:String = getString(R.string.favorite)
        setActionBarTitle(title)

        binding.tvUsers.layoutManager = LinearLayoutManager(this)
        binding.tvUsers.setHasFixedSize(true)
        adapter = UsersFavoriteAdapter(this)
        binding.tvUsers.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.dataUsers = list
            }
        }
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (notes.size > 0) {
                adapter.dataUsers = notes
                binding.emptyView.visibility = View.INVISIBLE
            } else {
                adapter.dataUsers = ArrayList()
                binding.emptyView.visibility = View.VISIBLE
                val text = getString(R.string.no_data)
                showSnackbarMessage(text)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.dataUsers)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.tvUsers, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }
}