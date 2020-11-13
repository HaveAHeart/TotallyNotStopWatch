package com.example.totallynotstopwatch

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock.sleep
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*


class AsyncActivity : AppCompatActivity()  {
    private val timeLD: MutableLiveData<Int> = MutableLiveData()
    var sharedPref: SharedPreferences? = null
    private var task: AsyncTimeUpdater? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //just initialize
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        //initialize sharedPreferences with zero, or, if "counter" exists, with it
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val ed = sharedPref!!.edit()
        ed.putInt("counter", sharedPref!!.getInt("counter", 0))
        ed.apply()

        //post value from sharedPref to LiveData, set observer to change UI
        timeLD.postValue(sharedPref!!.getInt("counter", -1))
        timeLD.observe(this, Observer { newValue -> counterText.text = newValue.toString() })

        //start async task
        task = AsyncTimeUpdater()
        task!!.execute()
    }

    override fun onPause() {
        super.onPause()

        //save value from LiveData to sharedPreferences
        val ed = sharedPref!!.edit()
        ed.putInt("counter", timeLD.value!!)
        ed.apply()

        //Toast.makeText(this, "val is ${timeLD.value}", Toast.LENGTH_SHORT).show()

        //finish the task
        task!!.cancel(false)
    }







    inner class AsyncTimeUpdater : AsyncTask<Void, Int, Int>() {


        override fun doInBackground(vararg p0: Void?): Int {
            //get initialized in onResume() time value
            val sharedPref = sharedPref ?: getSharedPreferences("settings", Context.MODE_PRIVATE)
            var time = sharedPref.getInt("counter", 0)

            while (true) {
                Thread.sleep(1000)
                time++
                //can not call UI-methods from this func, should use publishProgress()
                publishProgress(time)

                if (isCancelled) break
            }
            return 0
        }

        override fun onProgressUpdate(vararg values: Int?) {
            //update LiveData value, so observer could update the UI
            timeLD.value = values[0]
        }


    }
}