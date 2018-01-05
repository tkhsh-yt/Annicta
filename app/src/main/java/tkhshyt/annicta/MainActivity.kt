package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val pageTitle = arrayOf("放送予定")
    var fragments = arrayOfNulls<Fragment>(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.elevation = 0F

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
    }

    fun getFragment(n: Int): Fragment {
        if (fragments[n] == null) {
            fragments[n] = ProgramFragment()
        }

        return fragments[n]!!
    }
}
