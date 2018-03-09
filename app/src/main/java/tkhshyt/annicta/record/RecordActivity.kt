package tkhshyt.annicta.record

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chibatching.kotpref.Kotpref
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_record.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Episode
import tkhshyt.annicta.MyApplication
import tkhshyt.annicta.R
import tkhshyt.annicta.event.RecordedEvent
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.page.Page
import tkhshyt.annicta.page.go
import tkhshyt.annicta.pref.UserConfig
import tkhshyt.annicta.pref.UserInfo
import javax.inject.Inject


class RecordActivity : AppCompatActivity() {

    @Inject
    lateinit var annict: AnnictService

    @Inject
    lateinit var message: MessageCreator

    val rating = arrayOf(null, "bad", "average", "good", "great")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        Kotpref.init(this)

        (this.application as? MyApplication)?.getComponent()?.inject(this)

        if (intent.hasExtra("episode")) {
            val episode = intent.getSerializableExtra("episode") as Episode
            setupView(episode)
        }
    }

    fun setupView(episode: Episode) {
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
            workIcon.setImageResource(R.drawable.ic_image_black_24dp)
        }
        workIcon.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                go(
                        Page.WORK,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this, workIcon, workIcon.transitionName).toBundle(),
                        { it.putExtra("work", episode.work) }
                )
            } else {
                go(Page.WORK, { it.putExtra("work", episode.work) })
            }
        }

        toolbarIcon.setOnClickListener {
            supportFinishAfterTransition()
        }

        setupViewDependOnEpisode(episode)
    }

    fun setupViewDependOnEpisode(episode: Episode) {
        val episodeText = "${episode.number_text} ${episode.title.orEmpty()}"
        episodeTitle?.text = episodeText
        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            val verticalOffsetRate = 0.2

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (appBar.totalScrollRange + verticalOffset < appBar.totalScrollRange * verticalOffsetRate) {
                    toolbarTitle.text = episodeText
                    toolbarTitle.visibility = View.VISIBLE
                    toolbarIcon.setBackgroundResource(R.drawable.circle_transparent_ripple)
                } else {
                    toolbarTitle.visibility = View.INVISIBLE
                    toolbarIcon.setBackgroundResource(R.drawable.circle_grey_ripple)
                }
            }
        })

        fun setPrevNextEpisodeButton(episode: Episode?, view: TextView?, listener: (View) -> Unit) {
            if (episode != null) {
                view?.text = episode.number_text
                view?.setOnClickListener(listener)
                view?.visibility = View.VISIBLE
            } else {
                view?.visibility = View.GONE
            }
        }
        prevEpisodeButton?.setOnClickListener { }
        nextEpisodeButton?.setOnClickListener { }

        val accessToken = UserInfo.accessToken
        if (accessToken != null) {
            annict.episodes(
                    access_token = accessToken,
                    filter_ids = episode.id.toString()
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val newEpisode = it.body().resources().last()
                    val prevNextEpisode = arrayOf(
                            Pair(newEpisode.prev_episode, prevEpisodeButton),
                            Pair(newEpisode.next_episode, nextEpisodeButton)
                    )
                    prevNextEpisode.forEach {
                        setPrevNextEpisodeButton(it.first, it.second, { _ ->
                            val episode = it.first
                            if (episode != null) {
                                setupViewDependOnEpisode(episode)
                            }
                        })
                    }
                }, { _ ->
                    message.create()
                        .context(this)
                        .message("前/次のエピソードの取得に失敗しました")
                        .build().show()
                })

            recordButton?.setOnClickListener {
                if (episode.id != null) {
                    annict.createRecord(
                            access_token = accessToken,
                            episode_id = episode.id,
                            comment = commentEditText?.text.toString(),
                            rating_state = rating[ratingSpinner.selectedItemPosition],
                            share_twitter = twitterToggleButton.isChecked,
                            share_facebook = facebookToggleButton.isChecked
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            message.create()
                                .context(this)
                                .message("記録しました")
                                .build().show()
                            EventBus.getDefault().post(RecordedEvent(it.body()))
                            finish()
                        }, {
                            message.create()
                                .context(this)
                                .message("記録に失敗しました")
                                .build().show()
                        })
                }
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(commentEditText?.windowToken, 0)
            }
        }

        val bundle = Bundle()
        episode.id?.let { bundle.putLong("episode_id", it) }
        val fragment = RecordListFragment()
        fragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()

        commentEditText?.setOnFocusChangeListener { _, _ ->
            recordCommentArea?.visibility = View.VISIBLE
        }

        twitterToggleButton?.isChecked = UserConfig.shareTwitter
        twitterToggleButton?.setOnClickListener {
            UserConfig.shareTwitter = twitterToggleButton.isChecked
        }

        facebookToggleButton?.isChecked = UserConfig.shareFacebook
        facebookToggleButton?.setOnClickListener {
            UserConfig.shareFacebook = facebookToggleButton.isChecked
        }

        val adapter = ArrayAdapter<String>(this, R.layout.item_rating)
        resources.getStringArray(R.array.rating_array).forEach {
            adapter.add(it)
        }
        ratingSpinner.adapter = adapter
    }
}