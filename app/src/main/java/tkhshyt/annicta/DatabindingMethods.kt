package tkhshyt.annicta

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.broadcast_bar.view.*
import tkhshyt.annicta.util.AnnictUtil
import java.util.*

@BindingAdapter(value = ["bind:imageUrl", "bind:placeholder", "bind:noImage", "bind:centerCrop", "bind:circleCrop"], requireAll = false)
fun ImageView.setImageUrl(imageUrl: String, placeholder: Drawable? = null, noImage: Drawable? = null, centerCrop: Boolean = false, circleCrop: Boolean = false) {
    if(imageUrl.isNotEmpty()) {

        var options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        if (placeholder != null) {
            options = options.placeholder(placeholder)
        }

        if(centerCrop) {
            options = options.centerCrop()
        }

        if(circleCrop) {
            options = options.circleCrop()
        }

        Glide.with(this.context)
            .load(imageUrl)
            .apply(options)
            .into(this)
    } else {
        if(placeholder != null) {
            this.setImageDrawable(placeholder)
        } else if(noImage != null) {
            this.setImageDrawable(noImage)
        }
    }
}

@BindingAdapter("bind:date")
fun TextView.setDate(date: Date) {
    this.text = AnnictUtil.textDateFormat.format(date)
}

@BindingAdapter("bind:pretty_date")
fun TextView.setPrettyDate(date: Date) {
    this.text = AnnictUtil.prettyDate(date)
}

@BindingAdapter("bind:rating")
fun TextView.setRating(rating: String?) {
    this.text = when(rating) {
        "bad" -> "良くない"
        "average" -> "普通"
        "good" -> "良い"
        "great" -> "とても良い"
        "no_select" -> "未選択"
        else -> ""
    }
}