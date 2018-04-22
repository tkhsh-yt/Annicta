package tkhshyt.annicta.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ActivityMainBinding
import tkhshyt.annicta.event.SeasonSpinnerSelectedEvent
import tkhshyt.annicta.layout.spinner.StatusAdapter
import tkhshyt.annicta.main.activity.ActivitiesFragment
import tkhshyt.annicta.main.programs.ProgramsFragment
import tkhshyt.annicta.main.works.SeasonSelectSpinner
import tkhshyt.annicta.main.works.WorksFragment
import tkhshyt.annicta.top.TopActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, MainNavigator {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private val viewModel: MainViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java) }

    private val binding by lazy { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }

    override fun supportFragmentInjector() = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        viewModel.navigator = this
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        setupToolbar()
        setupSeasonSpinner()
        setupTab()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupSeasonSpinner() {
        val seasons = resources.getStringArray(R.array.season_array)
        val adapter = StatusAdapter(this, R.layout.spinner_item_season, seasons)
        adapter.setDropDownViewResource(R.layout.spinner_item_season_dropdown)
        seasonSpinner.adapter = adapter
        seasonSpinner.setSelection(1)

        seasonSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if ((adapter.selectedItem != -1 && adapter.selectedItem != position) || position == adapter.count - 1) {
                    EventBus.getDefault().post(SeasonSpinnerSelectedEvent(SeasonSelectSpinner.values()[position]))
                }
                adapter.selectedItem = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
//        seasonSpinner.setSelection(1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.logout -> {
                viewModel.logout()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setupTab() {
        val tabTitle = resources.getStringArray(R.array.tab)
        val tabViews = arrayOf(R.layout.tab_broadcast, R.layout.tab_work, R.layout.tab_home)
        val fragments = arrayOf<Fragment>(
                ProgramsFragment.newInstance(),
                WorksFragment.newInstance(),
                ActivitiesFragment.newInstance()
        )
        val adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return tabTitle[position]
            }

            override fun getCount(): Int {
                return tabTitle.size
            }
        }
        pager.adapter = adapter
        pager.offscreenPageLimit = 2

        tabs.setupWithViewPager(pager)
        (0 until tabs.tabCount).forEach {
            tabs.getTabAt(it)?.setCustomView(tabViews[it])
        }
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.selectedTabPosition.postValue(tabs.selectedTabPosition)
            }
        })
    }

    override fun restart() {
        val intent = Intent(this, TopActivity::class.java)
        startActivity(intent)
        finish()
    }
}