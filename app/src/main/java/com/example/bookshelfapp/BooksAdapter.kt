package com.example.bookshelfapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class BooksAdapter(private val books: List<BookItem>) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        Log.d("BookAdapter", "Binding book: ${book.volumeInfo.title}")

        holder.titleTextView.text = book.volumeInfo.title
        book.volumeInfo.imageLinks?.thumbnail?.let {
            holder.thumbnailImageView.load(it.replace("http", "https"))
        }
    }
    override fun getItemCount() = books.size
    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.bookTitle)
        val thumbnailImageView: ImageView = view.findViewById(R.id.bookThumbnail)
    }
}
