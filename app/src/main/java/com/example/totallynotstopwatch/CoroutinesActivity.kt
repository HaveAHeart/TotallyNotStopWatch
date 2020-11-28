package com.example.totallynotstopwatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class CoroutinesActivity : AppCompatActivity() {
    private var job =  GlobalScope.launch {  }
    private val sharedPref: SharedPreferences? = null
    private val time: MutableLiveData<Int> = MutableLiveData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job.cancel() //cancelling the initial empty job coroutine

        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val ed = sharedPref.edit()
        ed.putInt("counter", sharedPref.getInt("counter", 0))
        ed.apply()

        time.postValue(sharedPref.getInt("counter", -1))
        time.observe(this, Observer { newValue -> counterText.text = newValue.toString() })
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = sharedPref ?: getSharedPreferences("settings", Context.MODE_PRIVATE)
        time.postValue(sharedPref.getInt("counter", -1))
        job = createJob()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }



    private fun createJob() = GlobalScope.launch {
        job = GlobalScope.launch {
            while (true) {
                delay(1000)
                val sharedPref = sharedPref ?: getSharedPreferences("settings", Context.MODE_PRIVATE)
                val t = sharedPref.getInt("counter", -1)
                val ed = sharedPref.edit()
                ed.putInt("counter", t + 1)
                ed.apply()

                time.postValue(t)
            }
        }
    }
}