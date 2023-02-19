package com.sorykhan.libroread

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sorykhan.libroread.adapter.BookAdapter
import com.sorykhan.libroread.database.BookApplication
import com.sorykhan.libroread.databinding.FragmentAllBooksBinding
import com.sorykhan.libroread.external.DocumentActivity
import com.sorykhan.libroread.utils.PdfUtils
import com.sorykhan.libroread.utils.startBookActivity
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
        _binding = FragmentAllBooksBinding.inflate(inflater, container, false)
        Log.i(TAG, "Creating the views of the fragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.allBooksRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        Log.i(TAG, "Linked layout to recyclerView")

        val bookAdapter = BookAdapter(viewModel, requireContext()) {
            Log.i(TAG, "Book item clicked")
            if (Build.VERSION.SDK_INT >= 30) {
                if (PdfUtils.hasAllFilesPermission()) {
                    startBookActivity(it)
                } else {
                    PdfUtils.requestFilePermission(requireContext())
                }
            }
            startBookActivity(it)
        }
        recyclerView.adapter = bookAdapter
        Log.i(TAG, "Linked adapter to it's recyclerView")

        lifecycle.coroutineScope.launch {
            viewModel.getAllBooks().collect {
                bookAdapter.submitList(it)
                Log.i(TAG, "Extracted the books from the DB and sent them to the adapter")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}