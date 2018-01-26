package tkhshyt.annicta

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_record.view.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.json.Episode
import tkhshyt.annicta.event.CreateRecord
import tkhshyt.annicta.event.CreateRecordEvent
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.pref.UserConfig
import javax.inject.Inject

class RecordDialogFragment : DialogFragment() {

    @Inject
    lateinit var message: MessageCreator

    val maxCommentLength = 1000
    val rating = arrayOf(null, "bad", "average", "good", "great")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = activity?.let { AlertDialog.Builder(it) }

        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val content = inflater.inflate(R.layout.fragment_record, null)

        builder?.setView(content)

        val ep = arguments?.get("episode") as Episode

        Glide.with(this)
            .load(ep.work?.images?.recommended_url)
            .into(content.title_icon)

        content.title.text = ep.work?.title
        val episodeName = "${ep.number_text} ${ep.title.orEmpty()}"
        content.episode.text = episodeName

        val adapter = ArrayAdapter<String>(context, R.layout.item_rating)
        resources.getStringArray(R.array.rating_array).forEach {
            adapter.add(it)
        }
        content.ratingSpinner.adapter = adapter

        content.comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    val countText = "${s.length}/$maxCommentLength"
                    content.count.text = countText
                }
            }
        })

        content.twitter.isChecked = UserConfig.shareTwitter
        content.twitter.setOnClickListener {
            UserConfig.shareTwitter = content.twitter.isChecked
        }

        content.facebook.isChecked = UserConfig.shareFacebook
        content.facebook.setOnClickListener {
            UserConfig.shareFacebook = content.facebook.isChecked
        }

        content.record.setOnClickListener {
            if (content.comment.text.length <= maxCommentLength) {
                val create = CreateRecord(
                        episode = ep,
                        rating_state = rating[content.ratingSpinner.selectedItemPosition],
                        comment = content.comment.text.toString(),
                        share_twitter = content.twitter.isChecked,
                        share_facebook = content.facebook.isChecked
                )
                EventBus.getDefault().post(CreateRecordEvent(create))
                dismiss()
            } else {
                message.create()
                    .context(context)
                    .message("コメントが長すぎます")
                    .build().show()
            }
        }

        return builder!!.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.setBackgroundDrawableResource(R.drawable.rounded_dialog)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}