package com.example.marvel_app.data.remote.responses.CharacterComics

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
)