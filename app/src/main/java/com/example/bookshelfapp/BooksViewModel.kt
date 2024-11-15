package com.example.bookshelfapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import retrofit2.HttpException
import kotlinx.coroutines.delay

class BooksViewModel(private val repository: BooksRepository) : ViewModel() {

    private val _books = MutableLiveData<List<BookItem>>()
    val books: LiveData<List<BookItem>> get() = _books
    private val maxRetryAttempts = 5
    private val defaultRetryDelayMillis = 2000L

    fun searchBooks(query: String) {
        viewModelScope.launch {
            var attempt = 0
            while (attempt < maxRetryAttempts) {
                try {

                    val allBooks = repository.searchBooks(query) ?: emptyList()

                    _books.value = allBooks.take(10)
                    return@launch
                } catch (e: HttpException) {
                    if (e.code() == 429) {
                        attempt++

                        val retryAfter = e.response()?.headers()?.get("Retry-After")
                        val delayMillis = retryAfter?.toLongOrNull()?.times(1000)
                            ?: (defaultRetryDelayMillis * attempt)

                        Log.d("BooksViewModel", "Rate limit exceeded, retrying in $delayMillis ms. Attempt: $attempt")

                        if (attempt < maxRetryAttempts) {
                            delay(delayMillis)
                        } else {
                            _books.value = emptyList()
                            return@launch
                        }
                    } else {
                        _books.value = emptyList()
                        return@launch
                    }
                }
            }
        }
    }
}
