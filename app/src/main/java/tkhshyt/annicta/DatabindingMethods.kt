package tkhshyt.annicta

import android.databinding.BindingAdapter
import android.support.v4.content.res.ResourcesCompat
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import tkhshyt.annicta.util.AnnictUtil
import java.util.*

object DatabindingMethods {

    @JvmStatic
    @BindingAdapter("bind:date")
    fun setDate(textView: TextView, date: Date) {
        textView.text = AnnictUtil.textDateFormat.format(date)
    }

    @JvmStatic
    @BindingAdapter(value = ["bind:imageUrl", "bind:placeholderId", "bind:centerCrop"], requireAll = false)
    fun setImageUrl(imageView: ImageView, imageUrl: String, placeholderId: Int, centerCrop: Boolean) {
        imageView.setImageDrawable(ResourcesCompat.getDrawable(imageView.context.resources, R.drawable.ic_image_black_24dp, null))
        if(imageUrl.isNotEmpty()) {
            val options = RequestOptions()
                .placeholder(placeholderId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
            val request = Glide.with(imageView.context).load(imageUrl)
            if (centerCrop) {
                request.apply(options.centerCrop()).into(imageView)
            } else {
                request.apply(options).into(imageView)
            }
        }
    }
}
