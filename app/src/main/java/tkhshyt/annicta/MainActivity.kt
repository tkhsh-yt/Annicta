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

class MainActivity : AppCompatActivity() {

    private val pageTitle = arrayOf("放送予定", "今期アニメ")
    private val tabViews = arrayOf(R.layout.tab_broadcast, R.layout.tab_work)
    private val fragments = arrayOf(ProgramFragment(), WorkFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SharePreferences を扱うためのライブラリの初期化
        Kotpref.init(this)

        // ツールバーに関する設定
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.title = pageTitle[0]

        // ViewPager
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
}

