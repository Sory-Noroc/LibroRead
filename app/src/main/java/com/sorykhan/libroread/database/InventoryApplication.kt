package com.sorykhan.libroread.database

import android.app.Application

class InventoryApplication: Application() {
    val database: BookRoomDatabase by lazy { BookRoomDatabase.getDatabase(this)}
}