package com.example.marvel_app.data.remote.responses.ListHeroes

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)