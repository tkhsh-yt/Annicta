package tkhshyt.annicta

import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chibatching.kotpref.Kotpref
import kotlinx.android.synthetic.main.activity_work.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Work
import tkhshyt.annicta.event.StartRecordActivityEvent
import tkhshyt.annicta.page.Page
import tkhshyt.annicta.page.go
import javax.inject.Inject

class WorkActivity : AppCompatActivity() {

    @Inject
    lateinit var annict: AnnictService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)

        Kotpref.init(this)

        // DI
        (this.application as? DaggerApplication)?.getComponent()?.inject(this)

        toolbarIcon.setOnClickListener {
            supportFinishAfterTransition()
        }

        if (intent.hasExtra("work")) {
            val work = intent.getSerializableExtra("work") as Work

            appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                val verticalOffsetRate = 0.2

                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    if (appBar.totalScrollRange + verticalOffset < appBar.totalScrollRange * verticalOffsetRate) {
                        toolbarTitle.text = work.title
                        toolbarTitle.visibility = View.VISIBLE
                        toolbarIcon.setBackgroundResource(R.drawable.circle_transparent_ripple)
                    } else {
                        toolbarTitle.visibility = View.INVISIBLE
                        toolbarIcon.setBackgroundResource(R.drawable.circle_grey_ripple)
                    }
                }
            })

            var imageUrl: String? = null
            if (work.images?.recommended_url != null && work.images.recommended_url.isNotBlank()) {
                imageUrl = work.images.recommended_url
            } else if (work.images?.twitter?.image_url != null && work.images.twitter.image_url.isNotBlank()) {
                imageUrl = work.images.twitter.image_url
            }
            if (imageUrl != null) {
                Glide.with(this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(workIcon)
            } else {
                workIcon.setImageResource(R.drawable.ic_image_black_24dp)
            }

            val episodeListFragment = EpisodeListFragment()
            val args = Bundle()
            args.putSerializable("work", work)
            work.id?.let { args.putLong("work_id", it) }
            episodeListFragment.arguments = args
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, episodeListFragment)
            transaction.commit()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStartActivityEvent(event: StartRecordActivityEvent) {
        val episode = event.episode

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            go(
                    Page.RECORD,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, workIcon, workIcon.transitionName).toBundle(),
                    { it.putExtra("episode", episode) }
            )
        } else {
            go(Page.RECORD, { it.putExtra("episode", episode) })
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}