package com.example.bookshelfapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksRepository(private val service: BooksApiService) {
    suspend fun searchBooks(query: String): List<BookItem>? {
        return withContext(Dispatchers.IO) {
            try {
                service.searchBooks(query).items
            } catch (e: Exception) {
                null
            }
        }
    }
}
