package com.chun.uetfood.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.chun.uetfood.R
import java.text.SimpleDateFormat

object CommonApp {
    @JvmStatic
    val FORMAT_TIME = SimpleDateFormat("DD/MM/yyyy")

    @JvmStatic
    @BindingAdapter(value= arrayOf("loadNormalImageLink"))
    fun loadNormalImageLink(img: ImageView, link: String?) {
        if (link == null) {
            img.setImageResource(R.drawable.logo)
            return
        }
        Glide.with(img.context)
            .load(link)
            .error(R.drawable.logo)
            .placeholder(R.drawable.logo)
            .into(img)
    }

    @JvmStatic
    @BindingAdapter(value= arrayOf("loadNormalImageResource"))
    fun loadNormalImageResource(img: ImageView, resource: Int?) {
        if (resource == null) {
            img.setImageResource(R.drawable.logo)
            return
        }
        Glide.with(img.context)
            .load(resource)
            .error(R.drawable.logo)
            .placeholder(R.drawable.logo)
            .into(img)
    }
}