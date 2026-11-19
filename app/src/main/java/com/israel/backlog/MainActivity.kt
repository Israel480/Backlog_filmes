package com.israel.backlog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.israel.backlog.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MediaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MediaAdapter { item ->
            startActivity(Intent(this, DetailActivity::class.java).putExtra("item", item))
        }
        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.rvResults.adapter = adapter

        binding.btnSearch.setOnClickListener { search() }
        binding.btnMyList.setOnClickListener { startActivity(Intent(this, ListActivity::class.java)) }
    }

    private fun search() {
        val query = binding.etQuery.text.toString().trim()
        if (query.isEmpty()) {
            binding.etQuery.error = "Digite o nome de um filme ou serie"
            return
        }
        showLoading()
        RetrofitClient.api.search(BuildConfig.TMDB_KEY, query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (!response.isSuccessful) { showError("Erro do servico (codigo ${response.code()})"); return }
                val list = response.body()?.results.orEmpty()
                    .filter { it.media_type == "movie" || it.media_type == "tv" }
                if (list.isEmpty()) showError("Nenhum resultado encontrado") else showSuccess(list)
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showError("Falha de conexao. Verifique sua internet e tente novamente.")
            }
        })
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.tvMessage.visibility = View.GONE
        binding.rvResults.visibility = View.GONE
    }

    private fun showSuccess(list: List<MediaItem>) {
        binding.progress.visibility = View.GONE
        binding.tvMessage.visibility = View.GONE
        binding.rvResults.visibility = View.VISIBLE
        adapter.submit(list)
    }

    private fun showError(msg: String) {
        binding.progress.visibility = View.GONE
        binding.rvResults.visibility = View.GONE
        binding.tvMessage.text = msg
        binding.tvMessage.visibility = View.VISIBLE
    }
}
