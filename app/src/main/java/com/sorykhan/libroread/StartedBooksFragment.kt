package com.sorykhan.libroread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sorykhan.libroread.database.BookApplication
import com.sorykhan.libroread.databinding.FragmentStartedBooksBinding
import com.sorykhan.libroread.viewmodels.BookListViewModel
import com.sorykhan.libroread.viewmodels.BookListViewModelFactory

class StartedBooksFragment : Fragment() {

    private var _binding: FragmentStartedBooksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
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
//        val slideshowViewModel =
//            ViewModelProvider(this).get(StartedBooksViewModel::class.java)

        _binding = FragmentStartedBooksBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textSlideshow
//        slideshowViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}