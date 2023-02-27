package com.sorykhan.libroread

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.sorykhan.libroread.database.BookApplication
import com.sorykhan.libroread.databinding.ActivityMainBinding
import com.sorykhan.libroread.viewmodels.AllBooksViewModel
import com.sorykhan.libroread.viewmodels.BookListViewModelFactory

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: AllBooksViewModel by viewModels {
        BookListViewModelFactory(
            (application as BookApplication).database
                .bookDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.uploadToDatabase()

        observeAllBooks()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
//        val navController = findNavController(R.id.nav_books_fragment)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_books_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_all_books, R.id.nav_favorites, R.id.nav_started_books
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun observeAllBooks() {
        viewModel.allBooks.observe(this) {
            val bitmapsHashMap = mutableMapOf<String, Bitmap?>()
            viewModel.allBooks.observe(this) { books ->
                // Generate the bitmap for the current PDF path
                var changes = 0
                for (book in books) {
                    if (viewModel.coverMap.value?.containsKey(book.bookPath) != true) {
                        // If the cover was not added yet
                        val bitmap = viewModel.getBookCover(this, book.bookPath)

                        // Add the bitmap to the list
                        bitmapsHashMap[book.bookPath] = bitmap
                        changes = 1
                        Log.i(TAG, "Adding new entry to bookCovers map")
                    }
                }
                if (changes == 1) {
                    // Update the LiveData with the new list of bitmaps
                    viewModel.coverMap.value = bitmapsHashMap
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_books_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}