package com.example.myapplication.Models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ImageApi {
    private const val API_ENDPOINT = "https://acharyaprashant.org/api/v2/content/misc/media-coverages?limit=100"
    private var cachedImageUrls: List<String>? = null

    suspend fun fetchImageUrls(): List<String> {
        if (cachedImageUrls != null) {
            return cachedImageUrls!!
        }
        val imageUrls = fetchDataFromApi()
        cachedImageUrls = imageUrls
        return imageUrls
    }

    private suspend fun fetchDataFromApi(): List<String> {
        delay(2000)
        return withContext(Dispatchers.IO) {
            val response = makeHttpRequest(API_ENDPOINT)
            parseResponse(response)
        }
    }

    private fun makeHttpRequest(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        val response = StringBuilder()

        try {
            connection.requestMethod = "GET"
            BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
            }
        } finally {
            connection.disconnect()
        }

        return response.toString()
    }

    private fun parseResponse(response: String): List<String> {
        val urls = mutableListOf<String>()

        try {
            val jsonArray = JSONArray(response)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val thumbnailObject = jsonObject.optJSONObject("thumbnail")
                if (thumbnailObject != null) {
                    val domain = thumbnailObject.getString("domain")
                    val basePath = thumbnailObject.getString("basePath")
                    val key = thumbnailObject.getString("key")
                    val imageUrl = "$domain/$basePath/0/$key"
                    urls.add(imageUrl)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return urls
    }
}