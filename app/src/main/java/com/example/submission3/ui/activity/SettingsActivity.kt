package com.example.submission3.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.submission3.R
import com.example.submission3.alarm.AlarmReceiver
import com.example.submission3.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "SettingPrefs"
        private const val DAILY = "daily"
    }

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.settings)

        alarmReceiver = AlarmReceiver()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        changeSwitch()
        binding.btnSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setDailyReminder(this, AlarmReceiver.TYPE_REPEATING, getString(R.string.message))
            }else {
                alarmReceiver.cancelAlarm(this)
            }
            saveReminder(isChecked)
        }
    }

    private fun changeSwitch() {
        binding.btnSwitch.isChecked = sharedPreferences.getBoolean(DAILY, false)
    }

    private fun saveReminder(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(DAILY, value)
        editor.apply()
    }
}