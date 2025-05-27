package com.example.serebro_gallery.data

import retrofit2.http.GET

interface ExhibitionApi {
    @GET("photo")
    suspend fun getExhibitionsPage(): String
}