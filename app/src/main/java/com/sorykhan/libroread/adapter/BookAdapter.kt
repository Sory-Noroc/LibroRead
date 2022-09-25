package com.sorykhan.libroread.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sorykhan.libroread.R
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.databinding.BookListItemBinding
import com.sorykhan.libroread.utils.getStringMemoryFormat

class BookAdapter(private val onItemClicked: (Book) -> Unit): ListAdapter<Book, BookAdapter.ItemViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ItemViewHolder(private var binding: BookListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.bookTitle.text = book.bookName
            binding.bookSize.text = getStringMemoryFormat(book.bookSize)
            binding.bookProgressBar.progress = book.bookProgress
            binding.bookProgressBar.max = book.bookPages
            binding.favoriteButton.setImageResource(getFavoriteImageResource(book.isFavorite))
        }
    }

    private fun getFavoriteImageResource(isFavorite: Boolean): Int {
        return if (isFavorite) {
            R.drawable.ic_baseline_star_24
        } else {
            R.drawable.ic_baseline_star_outline_24
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val viewHolder = ItemViewHolder(
            BookListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}