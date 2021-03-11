package com.dendi.githubusers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailUser : AppCompatActivity() {
    companion object{
        var EXTRA_DATA = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        val actionbar = supportActionBar
        actionbar!!.title = " "
        actionbar.setDisplayHomeAsUpEnabled(true)

        val tvImage:ImageView = findViewById(R.id.img_photo)
        val tvName:TextView = findViewById(R.id.name)
        val tvUserName:TextView = findViewById(R.id.user_name)
        val tvFav:TextView = findViewById(R.id.detail_following)
        val tvCompany:TextView = findViewById(R.id.user_company)
        val tvLocation:TextView = findViewById(R.id.user_location)

        val person = intent.getParcelableExtra<User>(EXTRA_DATA) as User

        Glide.with(this)
                .load(person.photo)
                .apply(RequestOptions())
                .into(tvImage)
        tvName.text = person.name
        tvUserName.text = person.userName

        val user_detail = "${person.followers} followes. ${person.following} following. ${person.repository} repository"

        tvFav.text = user_detail
        tvCompany.text = person.company
        tvLocation.text = person.loacation

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.share_item -> {
                Toast.makeText(this, "Kamu memilih share", Toast.LENGTH_SHORT).show()
                shareItem()
            }
        }
    }

    private fun shareItem(){
        val personShare = intent.getParcelableExtra<User>(EXTRA_DATA) as User

        val name = personShare.name
        val userName = personShare.userName
        val following = personShare.following
        val followers = personShare.followers
        val repository = personShare.repository
        val company = personShare.company
        val location = personShare.loacation

        val user_detail = "$followers followes. $following following. $repository repository"

        val message = "Nama : $name ( $userName ),\n$user_detail \n\nCompany : $company \nLocation : $location"

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"

        startActivity(Intent.createChooser(intent, "Share using .."))
    }
}