package com.sorykhan.libroread.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sorykhan.libroread.R
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.utils.getStringMemoryFormat

class BookItemAdapter(private val context: Context, private val bookList: List<Book>): RecyclerView.Adapter<BookItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val bookTitle = view.findViewById<TextView>(R.id.book_title)
        val bookSize = view.findViewById<TextView>(R.id.book_size)
        val progressBar = view.findViewById<ProgressBar>(R.id.book_progress_bar)

        init {
            view.findViewById<ImageView>(R.id.book_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = bookList[position]
        holder.bookTitle.text = item.bookName
        holder.bookSize.text = getStringMemoryFormat(item.bookSize)
        holder.progressBar.progress = item.bookProgress
    }

    override fun getItemCount() = bookList.size
}