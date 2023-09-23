package com.example.movieapp.presentation.moviedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.databinding.FragmentMovieDetailsBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMovieDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val movie = MovieDetailsFragmentArgs.fromBundle(requireArguments()).selectedMovie

        binding.movie = movie
        var genres = ""
        movie.genresList.forEachIndexed { index, s ->
            run {
                if (index != movie.genresList.lastIndex) {
                    genres += s + ", "
                } else {
                    genres += s
                }
            }
        }

        binding.genres = genres

        return binding.root
    }
}