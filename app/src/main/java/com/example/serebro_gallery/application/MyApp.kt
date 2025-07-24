package com.example.serebro_gallery.application

import android.app.Application
import ru.null_checkers.data.local.database.AppDatabase
import ru.null_checkers.data.local.repository.PhotoRepositoryImpl

class MyApp : Application() {
    val database by lazy { AppDatabase.Companion.getDatabase(this) }
    val photoRepository by lazy { PhotoRepositoryImpl(database.photoDao()) }
}