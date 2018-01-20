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

    private val pageTitle = arrayOf("放送予定")
    private val pageIcon = arrayOf(R.drawable.ic_rss_feed)
    private val fragments = arrayOfNulls<Fragment>(1)

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

        tabs.getTabAt(0)?.setCustomView(R.layout.tab_broadcast)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.refresh, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_refresh -> {
                when (tabs.selectedTabPosition) {
                // ハードコーディングを後でやめる
                // 列挙とか使って，タブの管理と統一するのがいい？
                    0 -> {
                        EventBus.getDefault().post(ReloadProgramListEvent())
                    }
                }
            }
        }
        return true
    }

    fun getFragment(n: Int): Fragment {
        if (fragments[n] == null) {
            fragments[n] = ProgramFragment()
        }

        return fragments[n]!!
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

