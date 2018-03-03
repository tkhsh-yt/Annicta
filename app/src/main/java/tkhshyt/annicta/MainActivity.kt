package tkhshyt.annicta

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.chibatching.kotpref.Kotpref
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annicta.event.SeasonSpinnerSelectedEvent
import tkhshyt.annicta.page.Page
import tkhshyt.annicta.page.go

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private val pageTitle = arrayOf("放送予定", "シーズン", "アクティビティ")
    private val tabViews = arrayOf(R.layout.tab_broadcast, R.layout.tab_work, R.layout.tab_home)
    private val fragments = arrayOf(ProgramListFragment(), WorkListFragment(), ActivityListFragment())

    private var selectedItem = 1

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
        toolbarTitle.text = pageTitle[0]

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
        pager.offscreenPageLimit = adapter.count

        tabs.setupWithViewPager(pager)
        tabs.addOnTabSelectedListener(this)

        (0 until tabs.tabCount).forEach {
            tabs.getTabAt(it)?.setCustomView(tabViews[it])
        }

        setupSeasonToolbar()

        val item = PrimaryDrawerItem().withIdentifier(1).withName("ライセンス")
        val result = DrawerBuilder().withActivity(this)
            .addDrawerItems(item)
            .build()
        item.withOnDrawerItemClickListener { _, _, _ ->
            result.closeDrawer()
            go(Page.LICENSE)
            true
        }
    }

    fun setupSeasonToolbar() {
        val adapter = object : ArrayAdapter<String>(baseContext, R.layout.spinner_item_season, resources.getStringArray(R.array.season_array)) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view: View?
                if (convertView == null) {
                    view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.spinner_item_season, null)
                    view.findViewById<TextView>(R.id.season)?.text = getItem(position)
                    return view
                }
                convertView.findViewById<TextView>(R.id.season)?.text = getItem(position)

                return convertView
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = super.getDropDownView(position, convertView, parent)
                if (position == selectedItem) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_700))
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_800))
                }
                return view
            }
        }
        adapter.setDropDownViewResource(R.layout.spinner_item_season_dropdown)

        toolbarSpinner.adapter = adapter

        toolbarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if ((selectedItem != -1 && selectedItem != position) || position == adapter.count - 1) {
                    EventBus.getDefault().post(SeasonSpinnerSelectedEvent(SeasonSpinner.values()[position]))
                }
                selectedItem = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        toolbarSpinner.setSelection(selectedItem)
    }

    fun getFragment(n: Int): Fragment {
        return fragments[n] as Fragment
    }

    // OnTabSelectedListener

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val position = tabs.selectedTabPosition
        toolbarTitle.text = pageTitle[position]

        when (position) {
            0, 2 -> {
                toolbarTitle.visibility = View.VISIBLE
                toolbarSpinner.visibility = View.GONE
            }
            1 -> {
                toolbarTitle.visibility = View.GONE
                toolbarSpinner.visibility = View.VISIBLE
            }
        }
    }
}

