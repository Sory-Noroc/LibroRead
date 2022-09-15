package com.sorykhan.libroread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.sorykhan.libroread.adapter.BookItemAdapter
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.database.BookApplication
import com.sorykhan.libroread.databinding.FragmentAllBooksBinding
import com.sorykhan.libroread.viewmodels.BookListViewModel
import com.sorykhan.libroread.viewmodels.BookListViewModelFactory

class AllBooksFragment : Fragment() {

    private var _binding: FragmentAllBooksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var datasource: List<Book>
    private val sharedViewModel: BookListViewModel by activityViewModels {
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
//        val sharedViewModel = ViewModelProvider(this).get(BookListViewModel::class.java)
        datasource = sharedViewModel.getAllBooks().value ?: emptyList()

        _binding = FragmentAllBooksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.allBooksRecyclerView
        recyclerView.adapter = BookItemAdapter(requireContext(), datasource)
        recyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}