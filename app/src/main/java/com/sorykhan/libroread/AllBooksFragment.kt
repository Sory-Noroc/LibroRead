package com.sorykhan.libroread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sorykhan.libroread.adapter.BookItemAdapter
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.databinding.FragmentAllBooksBinding
import com.sorykhan.libroread.viewmodels.BookListViewModel

class AllBooksFragment : Fragment() {

    private var _binding: FragmentAllBooksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var datasource: List<Book>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedViewModel = ViewModelProvider(this).get(BookListViewModel::class.java)
        datasource = sharedViewModel.getAllBooks().value ?: emptyList()

        _binding = FragmentAllBooksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.allBooksRecyclerView
        recyclerView.adapter = BookItemAdapter(requireContext(), datasource)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}