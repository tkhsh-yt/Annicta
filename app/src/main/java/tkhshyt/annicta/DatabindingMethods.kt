package tkhshyt.annicta

import android.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.broadcast_bar.view.*
import tkhshyt.annicta.util.AnnictUtil
import java.util.*

@BindingAdapter(value = ["bind:imageUrl", "bind:placeholderId", "bind:centerCrop", "bind:circleCrop"], requireAll = false)
fun ImageView.setImageUrl(imageUrl: String, placeholderId: Int? = null, centerCrop: Boolean = false, circleCrop: Boolean = false) {
    if(imageUrl.isNotEmpty()) {

        var options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        if (placeholderId != null) {
            options = options.placeholder(placeholderId)
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
    }
}

@BindingAdapter("bind:date")
fun TextView.setDate(date: Date) {
    this.text = AnnictUtil.textDateFormat.format(date)
}
