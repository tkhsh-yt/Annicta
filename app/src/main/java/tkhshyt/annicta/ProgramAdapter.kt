package tkhshyt.annicta

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_program.view.*
import tkhshyt.annict.json.Program
import tkhshyt.annicta.utils.Utils

class ProgramAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list = ArrayList<Program>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.item_program, parent, false)
        return ProgramViewHolder(inflate, parent?.context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ProgramViewHolder) {
            holder.bindProgram(list[position])
        }
    }

    class ProgramViewHolder(item: View?, private val context: Context?) : RecyclerView.ViewHolder(item), View.OnClickListener {

        init {
            item?.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Toast.makeText(context, "touched", Toast.LENGTH_LONG).show()
        }

        fun bindProgram(program: Program) {
            var imageUrl: String? = null
            if (context != null) {
                if (program.work.images?.twitter?.image_url != null && program.work.images.twitter.image_url.isNotBlank()) {
                    imageUrl = program.work.images.twitter.image_url
                } else if (program.work.images?.recommended_url != null && program.work.images.recommended_url.isNotBlank()) {
                    imageUrl = program.work.images.recommended_url
                }
                if (imageUrl != null) {
                    Glide.with(context)
                        .load(imageUrl)
                        .into(itemView.title_icon)
                } else {
                    itemView.title_icon.setImageResource(R.drawable.ic_broken_image)
                }
            }
            itemView.start_at.text = Utils.textDateFormat.format(program.started_at)
            itemView.channel.text = program.channel.name
            itemView.title.text = program.work.title
            val title = "${program.episode.number_text} ${program.episode.title.orEmpty()}"
            itemView.episode.text = title
            if (program.is_rebroadcast) {
                itemView.rebroadcast.visibility = View.VISIBLE
            } else {
                itemView.rebroadcast.visibility = View.GONE
            }
        }
    }
}

