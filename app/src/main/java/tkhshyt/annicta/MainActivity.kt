package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annicta.event.ReloadProgramListEvent
import tkhshyt.annicta.event.subscribe.RecordEventSubscriber


class MainActivity : AppCompatActivity(), RecordEventSubscriber {

    override val baseActivity: AppCompatActivity = this

    private val pageTitle = arrayOf("放送予定", "今期アニメ")
    private val tabViews = arrayOf(R.layout.tab_broadcast, R.layout.tab_work)
    private val fragments = arrayOf(ProgramFragment(), WorkFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

