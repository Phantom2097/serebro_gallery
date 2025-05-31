package com.example.serebro_gallery.data

import retrofit2.http.GET

interface PartnersApi {
    @GET("partnery")
    suspend fun getPartnersPage(): String
}