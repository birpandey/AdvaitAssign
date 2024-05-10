# Image Loading Android App

This Android application efficiently loads and displays images in a scrollable grid. It supports lazy loading of images, memory caching, disk caching, and gracefully handles network errors and image loading failures.

## Features

- Display images in a scrollable grid with lazy loading.
- Asynchronous image loading from a given API endpoint.
- Memory caching for efficient retrieval of images.
- Disk caching to store images retrieved from the API.
- Graceful error handling for network errors and image loading failures.

## Technologies Used

- Kotlin
- Android SDK
- RecyclerView
- AsyncTask
- HttpURLConnection
- LruCache
- File storage for disk caching

## Setup Instructions

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the project on an Android emulator or physical device.

## Usage

1. Upon launching the app, images will start loading in the grid.
2. Scroll through the grid to see more images.
3. Images are loaded lazily as you scroll, ensuring smooth performance.
4. In case of network errors or image loading failures, informative error messages will be displayed.
