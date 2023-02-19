package com.example.movieapp.presentation.movieslist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.example.app.R

import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Picasso.with(imgView.context)
            .load(imgUri)
            .placeholder(R.drawable.outline_image_24)
            .error(R.drawable.outline_broken_image_24)
            .into(imgView)
    }
}

@BindingAdapter("loading")
fun bindLoadingItem(view: View, isLoading: Boolean) {
    if(isLoading){
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("favourite")
fun bindFavourite(button: ToggleButton, isFavourite: Boolean) {
    if(isFavourite){
        button.setBackgroundResource(R.drawable.round_favorite_24)
    } else {
        button.setBackgroundResource(R.drawable.round_not_filled_favorite_24)
    }
}

@BindingAdapter("rate")
fun bindRate(textView: TextView, rate: Double) {
    textView.text = rate.toString()
}
