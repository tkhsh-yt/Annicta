package tkhshyt.annicta.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ActivityAuthBinding
import tkhshyt.annicta.databinding.ActivityMainBinding
import tkhshyt.annicta.main.programs.ProgramsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, TabLayout.OnTabSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private val viewModel: MainViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java) }

    private val binding by lazy { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        setupTab()
    }

    override fun supportFragmentInjector() = fragmentInjector

    private fun setupTab() {
        val tabTitle = resources.getStringArray(R.array.tab)
        val tabViews = arrayOf(R.layout.tab_broadcast, R.layout.tab_work, R.layout.tab_home)
        val fragments = arrayOf(
                ProgramsFragment.newInstance(),
                ProgramsFragment.newInstance(),
                ProgramsFragment.newInstance()
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

        tabs.setupWithViewPager(pager)
        (0 until tabs.tabCount).forEach {
            tabs.getTabAt(it)?.setCustomView(tabViews[it])
        }
        tabs.addOnTabSelectedListener(this)
    }

    // OnTabSelectedListener

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewModel.selectedTabPosition.postValue(tabs.selectedTabPosition)
    }

}