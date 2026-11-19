package com.israel.backlog

import java.io.Serializable

// Modelo que espelha o JSON do endpoint /search/multi da TMDB
data class MediaItem(
    val id: Int,
    val title: String?,          // filmes
    val name: String?,           // series
    val overview: String?,
    val poster_path: String?,
    val media_type: String?,     // "movie" ou "tv"
    val vote_average: Double?,
    val release_date: String?,
    val first_air_date: String?
) : Serializable {
    fun displayTitle() = title ?: name ?: "Sem titulo"
    fun year() = (release_date ?: first_air_date)?.take(4).orEmpty()
    fun typeLabel() = if (media_type == "tv") "Serie" else "Filme"
    fun posterUrl() = poster_path?.let { "https://image.tmdb.org/t/p/w342$it" }
    fun key() = "${media_type}_$id"
}

data class SearchResponse(val results: List<MediaItem>)
