package com.example.movieapp.presentation.movieslist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.FragmentMoviesListBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MoviesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoviesListFragment : Fragment() {

    val viewModel: MoviesListViewModel by lazy {
        ViewModelProvider(this)[MoviesListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMoviesListBinding.inflate(inflater)
        binding.lifecycleOwner = this


        setHasOptionsMenu(true)

        val adapter = MoviesAdapter(MoviesListener(clickListener = {movieId ->
            val selectedMovie = viewModel.onMovieClicked(movieId)
            this.findNavController().navigate(MoviesListFragmentDirections.actionShowDetail(selectedMovie!!))
        }, favouriteListener = { movieId, isFavourite ->
            viewModel.setFavouriteProperty(movieId, isFavourite)
        }
        ))
        binding.moviesRecycler.adapter = adapter
        viewModel.listStatus.observe(viewLifecycleOwner, Observer{
            it?.let {
                when(it){
                    MoviesApiStatus.DONE -> binding.moviesRecycler.post(Runnable { adapter.addLoadingFooter() })
                    else -> adapter.removeLoadingFooter()
                }
            }
        })
        viewModel.movies.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.setData(it)
                binding.statusLoadingWheel.visibility = View.GONE
            }
        })
        val paginationScrollListener = PaginationScrollListener(binding.moviesRecycler.layoutManager as LinearLayoutManager,
            loadMoreItems = { viewModel.getNextPage() }, isLoading = { viewModel.isLoading }
        )
        viewModel.isFav.observe(viewLifecycleOwner, Observer{
            it?.let {
                if (it){
                    binding.moviesRecycler.removeOnScrollListener(paginationScrollListener)
                } else {
                    binding.moviesRecycler.addOnScrollListener(paginationScrollListener)
                }
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            getString(R.string.show_favourite) -> {
                item.isChecked = !item.isChecked;
                viewModel.filterFavourite(item.isChecked)
            }
        }
        return true
    }
}