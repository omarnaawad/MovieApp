package com.example.movieapp.presentation.movieslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.MovieListItemBinding
import com.example.movieapp.presentation.model.Movie

class MoviesAdapter(val listener: MoviesListener) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    var currentList: MutableList<Movie> = ArrayList()
    enum class ItemStatus{LOADING, ITEM}
    private var loadingItemAdded: Boolean = false

    class ViewHolder private constructor(val binding: MovieListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun binding(clickListener: MoviesListener, item: Movie) {
            binding.movie = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList.get(position)
        holder.binding(listener,item)
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == currentList.size - 1 && loadingItemAdded) ItemStatus.LOADING.ordinal else ItemStatus.ITEM.ordinal
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun setData(movieList: List<Movie>){
        currentList.clear()
        currentList.addAll(movieList)
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        loadingItemAdded = true
        add(Movie())
    }

    fun removeLoadingFooter() {
        if(currentList.isNotEmpty()) {
            loadingItemAdded = false
            val position: Int = currentList.size - 1
            val result = currentList.get(position)
            if (result != null) {
                currentList.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun add(movie: Movie?) {
        if(currentList.isNotEmpty()) {
            currentList.add(movie!!)
            notifyItemInserted(currentList.size - 1)
        }
    }
}


class MoviesListener(val clickListener: (movieId: Long) -> Unit, val favouriteListener: (movieId: Long, isFavourite: Boolean) -> Unit) {
    fun onClick(movie: Movie) {
        clickListener(movie.id)
    }
    fun onClickFavourite(movie: Movie, isFavourite: Boolean) {
        movie.isFavourite = isFavourite
        favouriteListener(movie.id, isFavourite)
    }
}

class PaginationScrollListener(val layoutManager: LinearLayoutManager, val loadMoreItems: () -> Unit,
                               val isLoading: () -> Boolean) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }
}