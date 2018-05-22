package tkhshyt.annicta.work_info

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_work_info.*
import tkhshyt.annict.json.Work
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ActivityWorkInfoBinding
import javax.inject.Inject

class WorkInfoActivity : AppCompatActivity(), HasSupportFragmentInjector, WorkInfoNavigator {

    companion object {
        const val WORK = "work"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private val viewModel: WorkInfoViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(WorkInfoViewModel::class.java) }

    private val binding by lazy { DataBindingUtil.setContentView<ActivityWorkInfoBinding>(this, R.layout.activity_work_info) }

    override fun supportFragmentInjector() = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        if(intent.hasExtra(WORK)) {
            val work = intent.getSerializableExtra(WORK) as Work
            viewModel.work.value = work
            viewModel.navigator = this

            binding.viewModel = viewModel
            binding.setLifecycleOwner(this)

            setupToolbar()

            setupWorkInfoList()
        }
    }

    private fun setupToolbar() {
        appBar.addOnOffsetChangedListener { _, verticalOffset ->
            val verticalOffsetRate = 0.2

            if (appBar.totalScrollRange + verticalOffset < appBar.totalScrollRange * verticalOffsetRate) {
                viewModel.titleVisibility.postValue(true)
            } else {
                viewModel.titleVisibility.postValue(false)
            }
        }

        viewModel.titleVisibility.observe(this, Observer<Boolean> {
            if(viewModel.titleVisibility.value == true) {
                toolbarIcon.setBackgroundResource(R.drawable.circle_transparent_ripple)
            } else {
                toolbarIcon.setBackgroundResource(R.drawable.circle_grey_ripple)
            }
        })
    }

    private fun setupWorkInfoList() {
        val fragment = WorkInfoFragment.newInstance()
        val arguments = Bundle()
        arguments.putSerializable(WORK, viewModel.work.value)
        fragment.arguments = arguments
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    override fun onClickBackArrow() {
        finish()
    }
}