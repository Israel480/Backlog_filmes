package com.israel.backlog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.israel.backlog.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: MediaAdapter
    private var current = Status.QUERO_ASSISTIR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MediaAdapter { item ->
            startActivity(Intent(this, DetailActivity::class.java).putExtra("item", item))
        }
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter

        binding.btnTabWant.setOnClickListener { current = Status.QUERO_ASSISTIR; render() }
        binding.btnTabWatched.setOnClickListener { current = Status.ASSISTIDO; render() }
        binding.btnBack.setOnClickListener { finish() }
        binding.btnMenu.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }
    }

    // Atualiza ao voltar da tela de detalhes
    override fun onResume() { super.onResume(); render() }

    private fun render() {
        val list = Watchlist.byStatus(current)
        adapter.submit(list)
        binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }
}
