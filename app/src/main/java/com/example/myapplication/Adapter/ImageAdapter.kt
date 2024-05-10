package com.example.myapplication.Adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.io.BufferedInputStream
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class ImageAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    // Cache for loaded bitmaps to avoid reloading them
    private val bitmapCache = HashMap<String, Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false) as ImageView
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        // Load image from URL and set it to ImageView
        holder.bindImage(imageUrl, position)
    }

    override fun getItemCount(): Int = imageUrls.size

    inner class ImageViewHolder(itemView: ImageView) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView

        fun bindImage(imageUrl: String, position: Int) {
            // Check if the bitmap for this URL is already in cache
            if (bitmapCache.containsKey(imageUrl)) {
                imageView.setImageBitmap(bitmapCache[imageUrl])
            } else {
                // If not in cache, load the bitmap asynchronously
                ImageLoadTask(imageView, position).execute(imageUrl)
            }
        }
    }

    private inner class ImageLoadTask(imageView: ImageView, private val position: Int) :
        AsyncTask<String, Void, Bitmap?>() {
        private val imageViewReference: WeakReference<ImageView> = WeakReference(imageView)
        private var imageUrl: String? = null

        override fun doInBackground(vararg params: String?): Bitmap? {
            imageUrl = params[0]
            var bitmap: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream: InputStream = BufferedInputStream(connection.inputStream)
                bitmap = BitmapFactory.decodeStream(inputStream)
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            // Check if the ImageView is still available and the URL matches
            if (imageViewReference.get() != null && imageUrl == imageUrls[position]) {
                result?.let {
                    // Set the bitmap to ImageView
                    imageViewReference.get()!!.setImageBitmap(it)
                    // Add bitmap to cache
                    bitmapCache[imageUrl!!] = it
                }
            }
        }
    }
}

