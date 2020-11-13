package com.example.totallynotstopwatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class ThreadActivity : AppCompatActivity()  {
    var sharedPref: SharedPreferences? = null
    var timerThread: TimerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        //initialize sharedPreferences with zero, or, if "counter" exists, with it
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val ed = sharedPref!!.edit()
        ed.putInt("counter", sharedPref!!.getInt("counter", 0))
        ed.putInt("inter", sharedPref!!.getInt("inter", 0))
        ed.apply()

        counterText.text = sharedPref!!.getInt("counter", 0).toString()
        timerThread = TimerThread()
    }

    override fun onPause() {
        super.onPause()

        timerThread!!.interrupt()
        Toast.makeText(this, "interrupted for ${sharedPref!!.getInt("inter", -1)}", Toast.LENGTH_SHORT).show()
    }

    inner class TimerThread : Thread() {
        var t:Int = 0

        init {
            t = sharedPref!!.getInt("counter", 0)
            start()
        }

        override fun run() {
            super.run()
            try {
                while(true) {
                    sleep(1000)
                    t++
                    counterText.post { counterText.text = t.toString() }
                }
            } catch (e: InterruptedException) {
                val intCounter = sharedPref!!.getInt("inter", 0)

                val ed = sharedPref!!.edit()
                ed.putInt("counter", t)
                ed.putInt("inter", intCounter + 1)
                ed.apply()
            }
        }
    }
}