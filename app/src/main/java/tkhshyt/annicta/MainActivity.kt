package tkhshyt.annicta

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import com.chibatching.kotpref.Kotpref
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.AnnictService
import tkhshyt.annicta.event.subscribe.RecordEventSubscriber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), RecordEventSubscriber {

    @Inject
    override lateinit var annict: AnnictService

    override val baseActivity: AppCompatActivity = this

    private val pageTitle = arrayOf("放送予定", "今期アニメ")
    private val tabViews = arrayOf(R.layout.tab_broadcast, R.layout.tab_work)
    private val fragments = arrayOf(ProgramFragment(), WorkFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Kotpref.init(this)

        (application as? DaggerApplication)?.getComponent()?.inject(this)

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        setSupportActionBar(toolbar)
        supportActionBar?.title = pageTitle[0]

        val adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int): Fragment {
                return getFragment(position)
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return pageTitle[position]
            }

            override fun getCount(): Int {
                return pageTitle.size
            }
        }

        pager.adapter = adapter
        tabs.setupWithViewPager(pager)

        (0 until tabs.tabCount).forEach {
            tabs.getTabAt(it)?.setCustomView(tabViews[it])
        }
    }

    fun getFragment(n: Int): Fragment {
        return fragments[n] as Fragment
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}

