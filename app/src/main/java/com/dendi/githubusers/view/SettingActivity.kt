package com.dendi.githubusers.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.dendi.githubusers.R
import com.dendi.githubusers.databinding.ActivitySettingBinding
import com.dendi.githubusers.helper.AlarmReceiver

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private var isAlarm = false

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title: String = getString(R.string.setting)
        setActionBarTitle(title)

        alarmReceiver = AlarmReceiver()

        if(alarmReceiver.isAlarmSet(this)){
            isAlarm = true
        }

        binding.switchReminder.isChecked = isAlarm
        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val repeatTime = "09:00"
                val repeatMessage = getString(R.string.message_alarm)
                alarmReceiver.setRepeatingAlarm(this, repeatTime, repeatMessage )

                val text = getString(R.string.set_repeat)
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            } else {
                val text = getString(R.string.cancel_repeat)
                alarmReceiver.cancelAlarm(this)
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }

        binding.changeLanguage.setOnClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }
}