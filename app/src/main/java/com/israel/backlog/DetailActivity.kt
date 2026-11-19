package com.israel.backlog

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.israel.backlog.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getSerializableExtra("item") as? MediaItem
        if (item == null) { finish(); return }

        binding.tvTitle.text = item.displayTitle()
        binding.tvInfo.text = "${item.typeLabel()} • ${item.year()} • Nota ${item.vote_average?.let { "%.1f".format(it) } ?: "-"}"
        binding.tvOverview.text = item.overview?.takeIf { it.isNotBlank() } ?: "Sem sinopse disponivel."
        Glide.with(this).load(item.posterUrl()).into(binding.ivPoster)
        refreshStatus(item)

        binding.btnWant.setOnClickListener {
            Watchlist.set(item, Status.QUERO_ASSISTIR)
            Toast.makeText(this, "Adicionado em Quero assistir", Toast.LENGTH_SHORT).show()
            refreshStatus(item)
        }
        binding.btnWatched.setOnClickListener {
            Watchlist.set(item, Status.ASSISTIDO)
            Toast.makeText(this, "Marcado como assistido", Toast.LENGTH_SHORT).show()
            refreshStatus(item)
        }
        binding.btnRemove.setOnClickListener {
            Watchlist.remove(item)
            Toast.makeText(this, "Removido da lista", Toast.LENGTH_SHORT).show()
            refreshStatus(item)
        }
        binding.btnBack.setOnClickListener { finish() }
        binding.btnMenu.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }
    }

    private fun refreshStatus(item: MediaItem) {
        binding.tvStatus.text = when (Watchlist.statusOf(item)) {
            Status.QUERO_ASSISTIR -> "Status: quero assistir"
            Status.ASSISTIDO -> "Status: assistido"
            null -> "Status: fora da sua lista"
        }
    }
}
