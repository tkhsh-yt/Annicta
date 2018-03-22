package tkhshyt.annicta

import android.databinding.BindingAdapter
import android.support.v4.content.res.ResourcesCompat
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import tkhshyt.annicta.util.AnnictUtil
import java.util.*

object DatabindingMethods {

    @JvmStatic
    @BindingAdapter("bind:date")
    fun setDate(textView: TextView, date: Date) {
        textView.text = AnnictUtil.textDateFormat.format(date)
    }

    @JvmStatic
    @BindingAdapter(value = ["bind:imageUrl", "bind:placeholderId", "bind:centerCrop", "bind:circleCrop"], requireAll = false)
    fun setImageUrl(imageView: ImageView, imageUrl: String, placeholderId: Int, centerCrop: Boolean?, circleCrop: Boolean?) {
        if(imageUrl.isNotEmpty()) {

            var options = RequestOptions()
                .placeholder(placeholderId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            if(centerCrop == true) {
                options = options.centerCrop()
            }

            if(circleCrop == true) {
                options = options.circleCrop()
            }

            Glide.with(imageView.context)
                .load(imageUrl)
                .apply(options)
                .into(imageView)
        }
    }
}
