package com.example.marvel_app.data.remote

import com.example.marvel_app.data.remote.responses.Character.Character
import com.example.marvel_app.data.remote.responses.Comics.Comics
import com.example.marvel_app.data.remote.responses.ListHeroes.ListHeroes
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {
    @GET("characters")
    suspend fun getHeroList(
        @Query("nameStartsWith") name: String
    ): ListHeroes

    @GET("characters/{id}")
    suspend fun getHero(
        @Path("id") id: Int?
    ): Character

    @GET("characters/{id}/comics")
    suspend fun getHeroComics(
        @Path("id") id: Int?,
        @Query("limit") limit: Int
    ):Comics
}
