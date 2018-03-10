package tkhshyt.annicta

import android.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import tkhshyt.annicta.util.AnnictUtil
import java.util.*

object DatabindingMethods {

    @JvmStatic
    @BindingAdapter("android:text")
    fun setDate(textView: TextView, date: Date) {
        textView.text = AnnictUtil.prettyDate(date)
    }

    @JvmStatic
    @BindingAdapter(value = ["bind:imageUrl", "bind:placeholderId", "bind:centerCrop"], requireAll = false)
    fun setImageUrl(imageView: ImageView, imageUrl: String, placeholderId: Int, centerCrop: Boolean) {
        val options = RequestOptions()
            .placeholder(placeholderId)
        val request = Glide.with(imageView.context).load(imageUrl)
        if (centerCrop) {
            request.apply(options.centerCrop()).into(imageView)
        } else {
            request.apply(options).into(imageView)
        }
    }
}
