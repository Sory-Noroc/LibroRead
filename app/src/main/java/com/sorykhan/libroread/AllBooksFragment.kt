package com.sorykhan.libroread

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sorykhan.libroread.adapter.BookAdapter
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.database.BookApplication
import com.sorykhan.libroread.databinding.FragmentAllBooksBinding
import com.sorykhan.libroread.viewmodels.AllBooksViewModel
import com.sorykhan.libroread.viewmodels.BookListViewModelFactory
import kotlinx.coroutines.launch

private const val TAG = "AllBooksFragment"

class AllBooksFragment : Fragment() {

    private var _binding: FragmentAllBooksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var datasource: List<Book>

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
        Log.d(TAG, "Datasource: $datasource")

        _binding = FragmentAllBooksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.allBooksRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val bookAdapter = BookAdapter({
            TODO("When item clicked, go to activity of reading this book")
        })
        recyclerView.adapter = bookAdapter

        lifecycle.coroutineScope.launch {
            viewModel.getAllBooks().collect() {
                bookAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}