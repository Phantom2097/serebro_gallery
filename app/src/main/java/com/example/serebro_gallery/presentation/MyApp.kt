package com.example.serebro_gallery.presentation

import android.app.Application
import kotlin.getValue
import com.example.serebro_gallery.domain.repository.PhotoRepository
import com.example.serebro_gallery.data.AppDatabase

class MyApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val photoRepository by lazy { PhotoRepository(database.photoDao()) }
}