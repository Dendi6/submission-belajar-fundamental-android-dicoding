package com.dendi.githubusers

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter

    private lateinit var dataUserName: Array<String>
    private lateinit var dataName: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataCompany: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataFollower: Array<String>
    private lateinit var dataFollowing: Array<String>
    private lateinit var dataPhoto: TypedArray

    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.tv_github)
        adapter = UserAdapter(this)
        listView.adapter = adapter

        prepare()
        addItem()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val listDataUser = User(0,"","", "", "", "", "", "")
            listDataUser.photo = dataPhoto.getResourceId(position, position)
            listDataUser.userName = dataUserName[position]
            listDataUser.name = dataName[position]
            listDataUser.loacation = dataLocation[position]
            listDataUser.repository = dataRepository[position]
            listDataUser.company = dataCompany[position]
            listDataUser.followers = dataFollower[position]
            listDataUser.following = dataFollowing[position]

            val intent = Intent(this@MainActivity, DetailUser::class.java)
            intent.putExtra(DetailUser.EXTRA_DATA, listDataUser)

            this@MainActivity.startActivity(intent)
            Toast.makeText(this@MainActivity, users[position].name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun prepare() {
        dataPhoto = resources.obtainTypedArray(R.array.avatar)
        dataUserName = resources.getStringArray(R.array.username)
        dataName = resources.getStringArray(R.array.name)
        dataLocation = resources.getStringArray(R.array.location)
        dataCompany = resources.getStringArray(R.array.company)
        dataRepository = resources.getStringArray(R.array.repository)
        dataFollower = resources.getStringArray(R.array.followers)
        dataFollowing = resources.getStringArray(R.array.following)
    }

    private fun addItem() {
        for (position in dataName.indices) {
            val user = User(
                    dataPhoto.getResourceId(position, -1),
                    dataUserName[position],
                    dataName[position],
                    dataLocation[position],
                    dataCompany[position],
                    dataRepository[position],
                    dataFollower[position],
                    dataFollowing[position]
            )
            users.add(user)
        }
        adapter.users = users
    }
}