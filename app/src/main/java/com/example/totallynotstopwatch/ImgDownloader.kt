package com.example.totallynotstopwatch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dowmload_activity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.URL


class ImgDownloader: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dowmload_activity)
        button.setOnClickListener {
            //DownloadImageTask(imageView2)
            //    .execute("https://smartminds.ru/wp-content/uploads/2019/12/foto-2-kartinka-s-pozhelaniem-prekrasnogo-nastroeniya-na-ves-den.jpg")

            //runCoroutine()

            Picasso.with(this)
                .load("https://smartminds.ru/wp-content/uploads/2019/12/foto-2-kartinka-s-pozhelaniem-prekrasnogo-nastroeniya-na-ves-den.jpg")
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView2);
        }

    }

    inner class DownloadImageTask(var bmImage: ImageView) : AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg urls: String?): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val inp: InputStream = URL(urldisplay).openStream()
                publishProgress()
                mIcon11 = BitmapFactory.decodeStream(inp)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return mIcon11
        }

        override fun onPostExecute(result: Bitmap?) {
            bmImage.setImageBitmap(result)
        }

        override fun onProgressUpdate(vararg values: Void?) {
            Toast.makeText(this@ImgDownloader, "download: started", Toast.LENGTH_SHORT).show()
        }
    }

    fun runCoroutine() {
        GlobalScope.launch {
            val urldisplay = "https://smartminds.ru/wp-content/uploads/2019/12/foto-2-kartinka-s-pozhelaniem-prekrasnogo-nastroeniya-na-ves-den.jpg"
            var mIcon11: Bitmap? = null
            try {
                val inp: InputStream = URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(inp)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            imageView2.post {
                imageView2.setImageBitmap(mIcon11)
            }
        }
    }

}