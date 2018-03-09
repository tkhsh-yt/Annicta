package tkhshyt.annicta.work

import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.chibatching.kotpref.Kotpref
import kotlinx.android.synthetic.main.activity_work.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Work
import tkhshyt.annicta.MyApplication
import tkhshyt.annicta.R
import tkhshyt.annicta.event.StartRecordActivityEvent
import tkhshyt.annicta.event.UpdateStatusEvent
import tkhshyt.annicta.extension.defaultOn
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.page.Page
import tkhshyt.annicta.page.go
import tkhshyt.annicta.pool.WorkPool
import tkhshyt.annicta.pref.UserInfo
import javax.inject.Inject

class WorkActivity : AppCompatActivity() {

    @Inject
    lateinit var annict: AnnictService

    @Inject
    lateinit var message: MessageCreator

    @Inject
    lateinit var workPool: WorkPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)

        Kotpref.init(this)

        // DI
        (this.application as? MyApplication)?.getComponent()?.inject(this)

        toolbarIcon.setOnClickListener {
            supportFinishAfterTransition()
        }

        if (intent.hasExtra("work_id")) {
            val workId = intent.getLongExtra("work_id", 0)
            val work = workPool.getWork(workId)

            if (work != null) {
                setupWorkInfo(work)

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
    }

    fun setupWorkInfo(work: Work) {
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
                .into(workIcon)
        } else {
            workIcon.setImageResource(R.drawable.ic_image_black_24dp)
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

    @Subscribe
    fun onUpdateStatus(event: UpdateStatusEvent) {
        val accessToken = UserInfo.accessToken
        if (accessToken != null) {
            annict.updateState(
                    access_token = accessToken,
                    work_id = event.workId,
                    kind = event.status
            ).defaultOn()
                .subscribe({
                    workPool.updateWorkStatus(event.workId, event.status)
                    message.create()
                        .context(this)
                        .message(resources.getString(R.string.update_status))
                        .build().show()
                }, {
                    message.create()
                        .context(this)
                        .message(resources.getString(R.string.fail_to_update_status))
                        .build().show()
                })
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