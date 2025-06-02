package com.example.serebro_gallery.data

import retrofit2.http.GET
import retrofit2.http.Url

interface PhotoApi {
    @GET
    suspend fun getPhotosPage(@Url pageUrl: String?): String
}