package com.israel.backlog

enum class Status { QUERO_ASSISTIR, ASSISTIDO }

// Lista guardada em memoria (enquanto o app esta aberto)
object Watchlist {
    private val entries = linkedMapOf<String, Pair<MediaItem, Status>>()

    fun set(item: MediaItem, status: Status) { entries[item.key()] = item to status }
    fun remove(item: MediaItem) { entries.remove(item.key()) }
    fun statusOf(item: MediaItem): Status? = entries[item.key()]?.second
    fun byStatus(status: Status): List<MediaItem> =
        entries.values.filter { it.second == status }.map { it.first }
}
