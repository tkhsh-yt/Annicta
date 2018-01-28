package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.chibatching.kotpref.Kotpref
import kotlinx.android.synthetic.main.activity_record.*
import tkhshyt.annict.json.Episode
import android.support.design.widget.AppBarLayout

class RecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        Kotpref.init(this)

        setSupportActionBar(toolbar)
        title = ""

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        if (intent.hasExtra("episode")) {
            val episode = intent.getSerializableExtra("episode") as Episode

            appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                internal var isShow = false
                internal val verticalOffsetRate = 0.3

                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    if (appBar.totalScrollRange + verticalOffset < appBar.totalScrollRange * verticalOffsetRate) {
                        title = episode.work?.title
                        isShow = true
                    } else if (isShow) {
                        title = ""
                        isShow = false
                    }
                }
            })

            var imageUrl: String? = null
            if (episode.work?.images?.twitter?.image_url != null && episode.work.images.twitter.image_url.isNotBlank()) {
                imageUrl = episode.work.images.twitter.image_url
            } else if (episode.work?.images?.recommended_url != null && episode.work.images.recommended_url.isNotBlank()) {
                imageUrl = episode.work.images.recommended_url
            }
            if (imageUrl != null) {
                Glide.with(this)
                    .load(imageUrl)
                    .into(workIcon)
            } else {
                title = episode.work?.title
                workIcon.setImageResource(R.drawable.ic_image_black_24dp)
            }

            val bundle = Bundle()
            episode.id?.let { bundle.putLong("episode_id", it) }
            val fragment = RecordFragment()
            fragment.arguments = bundle

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
    }
}