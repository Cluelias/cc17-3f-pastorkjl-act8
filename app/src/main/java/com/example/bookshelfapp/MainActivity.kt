package com.example.bookshelfapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookshelfapp.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: BooksViewModel by viewModels {
        val service = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApiService::class.java)
        val repository = BooksRepository(service)
        BooksViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        viewModel.books.observe(this) { books ->
            Log.d("MainActivity", "Books received: ${books.size}")
            if (books.isNotEmpty()) {
                binding.recyclerView.adapter = BooksAdapter(books)
            } else {
                Log.d("MainActivity", "No books available to display.")
            }
        }
        viewModel.searchBooks("THE LORD OF THE RINGS SERIES")
    }
}