package com.sorykhan.libroread

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sorykhan.libroread.adapter.BookAdapter
import com.sorykhan.libroread.database.BookApplication
import com.sorykhan.libroread.databinding.FragmentFavoritesBinding
import com.sorykhan.libroread.utils.startBookActivity
import com.sorykhan.libroread.viewmodels.AllBooksViewModel
import com.sorykhan.libroread.viewmodels.BookListViewModelFactory
import kotlinx.coroutines.launch

private const val TAG = "FavoritesFragment"

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    private val viewModel: AllBooksViewModel by activityViewModels {
        BookListViewModelFactory(
            (activity?.application as BookApplication).database
                .bookDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        Log.i(TAG, "Creating views in the fragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.favoritesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val bookAdapter = BookAdapter(viewModel, requireContext()) {
            Log.i(TAG, "Book item clicked")
            startBookActivity(it)
        }

        recyclerView.adapter = bookAdapter
        Log.i(TAG, "Linked adapter to recyclerView")

        lifecycle.coroutineScope.launch {
            viewModel.getFavoriteBooks().collect {
                bookAdapter.submitList(it)
                Log.i(TAG, "Extracted the favourites from the DB and sent them to the adapter")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}