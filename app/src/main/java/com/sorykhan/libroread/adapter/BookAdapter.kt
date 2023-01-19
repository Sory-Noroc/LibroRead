package com.sorykhan.libroread.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.nfc.Tag
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sorykhan.libroread.R
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.databinding.BookListItemBinding
import com.sorykhan.libroread.utils.FormatUtils
import com.sorykhan.libroread.utils.getStringMemoryFormat
import com.sorykhan.libroread.viewmodels.AllBooksViewModel
import java.io.File
import java.io.FileNotFoundException

private const val TAG = "BookAdapter"

class BookAdapter(private val viewModel: AllBooksViewModel, private val context: Context, private val onItemClicked: (Book) -> Unit): ListAdapter<Book, BookAdapter.ItemViewHolder>(DiffCallback) {
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

    inner class ItemViewHolder(var binding: BookListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            Log.i(TAG, "Binding the book $book")
            if (!File(book.bookPath).exists()) {
                viewModel.deleteBook(book)
            }
            binding.bookImage.setImageBitmap(getBookCover(book.bookPath))
            binding.bookTitle.text = book.bookName
            binding.bookSize.text = context.getString(R.string.size_s, getStringMemoryFormat(book.bookSize))
            binding.progressView.text = context.getString(R.string.progress_s, FormatUtils.getProgressPercentage(book.bookProgress, book.bookPages))
            binding.favoriteButton.setImageResource(getFavoriteImageResource(book.isFavorite))
            binding.favoriteButton.setOnClickListener {
                Log.d(TAG, "Before updating favorite button")
                viewModel.updateIsFavorite(book.bookPath)
                Log.d(TAG, "After updating")
            }
            binding.deleteButton.setOnClickListener {
                val file = File(book.bookPath)
                file.delete()
                viewModel.deleteBook(book)
                Log.i(TAG, "Deleted book ${book.bookPath}")
            }
        }

        private fun getBookCover(pdfPath: String): Bitmap? {
            val file = File(pdfPath)
            var page:PdfRenderer.Page? = null
            var pdfRenderer: PdfRenderer? = null
            try {
                val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                pdfRenderer = PdfRenderer(fileDescriptor)
                page = pdfRenderer.openPage(0)
                val density = context.resources.displayMetrics.density
                val desiredWidth = (30 * density).toInt()
                val desiredHeight = (45 * density).toInt()
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true)
                page.render(scaledBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                return scaledBitmap
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "Wrong path/file $e")
            } catch (e: Exception) {
                Log.e(TAG, "Error when rendering book cover: $e")
                page?.close()
                pdfRenderer?.close()
            }
            return null
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
        Log.i(TAG, "Creating the ViewHolder")
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        viewHolder.itemView
        return viewHolder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.favoriteButton.setOnClickListener(null)
        holder.binding.deleteButton.setOnClickListener(null)
        holder.binding.favoriteButton.setImageDrawable(null)

        holder.bind(getItem(position))
        Log.i(TAG, "Binding the viewHolder")
    }
}