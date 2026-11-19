package com.israel.backlog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.israel.backlog.databinding.ItemMediaBinding

class MediaAdapter(
    private var items: List<MediaItem> = emptyList(),
    private val onClick: (MediaItem) -> Unit
) : RecyclerView.Adapter<MediaAdapter.VH>() {

    class VH(val b: ItemMediaBinding) : RecyclerView.ViewHolder(b.root)

    fun submit(list: List<MediaItem>) { items = list; notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.b.tvTitle.text = item.displayTitle()
        holder.b.tvSub.text = "${item.typeLabel()} • ${item.year()}"
        Glide.with(holder.b.root).load(item.posterUrl()).into(holder.b.ivPoster)
        holder.b.root.setOnClickListener { onClick(item) }
    }
}
