package com.example.myapplication.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel : ViewModel() {
    private val _imageUrls = MutableLiveData<List<String>>()
    val imageUrls: LiveData<List<String>> = _imageUrls

    fun fetchImageUrls() {
        CoroutineScope(Dispatchers.IO).launch {
            val urls = ImageApi.fetchImageUrls()
            _imageUrls.postValue(urls)
        }
    }
}
