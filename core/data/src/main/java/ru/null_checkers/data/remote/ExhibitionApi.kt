package ru.null_checkers.data.remote

import retrofit2.http.GET

interface ExhibitionApi {
    @GET("photo")
    suspend fun getExhibitionsPage(): String
}