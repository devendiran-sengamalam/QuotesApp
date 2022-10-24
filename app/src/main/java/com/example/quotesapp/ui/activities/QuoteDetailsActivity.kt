package com.example.quotesapp.ui.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.quotesapp.R
import com.example.quotesapp.databinding.ActivityQuoteDetailsBinding
import com.example.quotesapp.ui.viewmodels.QuotesDetailsViewModel
import kotlinx.android.synthetic.main.activity_quote_details.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors

class QuoteDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel : QuotesDetailsViewModel
    private lateinit var binding : ActivityQuoteDetailsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quote_details)
        viewModel = ViewModelProvider(this)[QuotesDetailsViewModel::class.java]

       val intent = intent
        val id = intent.getStringExtra("quoteID")
         val imagePoster = binding.imagePoster
        var imageUrl :String =""

        viewModel.getDetails(id)
        viewModel.quotesDetails.observe(this,  Observer { quotes->
            text_author.text = "Author:"+quotes.author
            text_content.text = quotes.content
           // text_author_slug.text =quotes.authorSlug
            text_length.text ="Length:" +quotes.length.toString()
            imageUrl = quotes.imageUrl

            Glide.with(imagePoster.context).load(quotes.imageUrl)
                .centerCrop()
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePoster)
        })

        binding.imageShare.setOnClickListener{
            val sendIntent:Intent = Intent().apply{
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,text_content.text)
                type = "text/plain"
            }
            val shareIntent=Intent.createChooser(sendIntent,"Today Quote:")
            startActivity(shareIntent)
        }
        // Declaring a Bitmap local
        var mImage: Bitmap?
        // Declaring and initializing an Executor and a Handler
        val myExecutor = Executors.newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())

        // When Button is clicked, executor will
        // fetch the image and handler will display it.
        // Once displayed, it is stored locally
        binding.saveButton.setOnClickListener {
            myExecutor.execute {
                mImage = mLoad(imageUrl)
                myHandler.post {
                   
                    if(mImage!=null){
                        SaveMediaToStorage(mImage)
                    }
                }
            }
        }

    }

    // Function to establish connection and load image
    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }

    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    // Function to save image on the device.

    private fun SaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Image saved successfully to your device" , Toast.LENGTH_SHORT).show()
        }
    }

}