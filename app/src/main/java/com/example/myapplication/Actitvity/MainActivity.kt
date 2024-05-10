package com.example.myapplication.Actitvity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.ImageAdapter
import com.example.myapplication.Models.ImageViewModel
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ImageViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        // Check network connectivity before fetching image URLs
        if (isNetworkConnected()) {
            // Observe image URLs from ViewModel
            viewModel.imageUrls.observe(this, Observer { imageUrls ->
                // Update RecyclerView with new image URLs
                recyclerView.adapter = ImageAdapter(imageUrls)
            })

            // Fetch image URLs
            viewModel.fetchImageUrls()
        } else {
            Toast.makeText(
                this,
                "No internet connection. Please check your network settings.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }
}
