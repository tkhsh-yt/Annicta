package tkhshyt.annicta.record

import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_record.*
import tkhshyt.annict.json.Episode
import tkhshyt.annict.json.Program
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ActivityRecordBinding
import tkhshyt.annicta.main.programs.ProgramsFragment
import tkhshyt.annicta.util.setupToast
import javax.inject.Inject

class RecordActivity : AppCompatActivity(), HasSupportFragmentInjector, RecordNavigator {

    companion object {
        const val EPISODE_ID = "episode_id"
        const val EPISODE = "episode"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RecordViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(RecordViewModel::class.java) }

    private val binding by lazy { DataBindingUtil.setContentView<ActivityRecordBinding>(this, R.layout.activity_record) }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        if (intent.hasExtra(EPISODE)) {
            viewModel.episode.observe(this, Observer {
                viewModel.setPrevNextEpisode()
            })

            viewModel.let {
                setupToast(this, it.toastMessage, Toast.LENGTH_SHORT)
            }

            val episode = intent.getSerializableExtra(EPISODE) as Episode
            viewModel.episode.value = episode
            viewModel.navigator = this

            binding.viewModel = viewModel
            binding.setLifecycleOwner(this)

            setupToolbar()
            setupRecordsFragment()
            setupRecordEdit()

            viewModel.onStart()
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

        // FIXME
        viewModel.titleVisibility.observe(this, Observer<Boolean> {
            if(viewModel.titleVisibility.value == true) {
                toolbarIcon.setBackgroundResource(R.drawable.circle_transparent_ripple)
            } else {
                toolbarIcon.setBackgroundResource(R.drawable.circle_grey_ripple)
            }
        })
    }

    private fun setupRecordsFragment() {
        val fragment = RecordsFragment.newInstance()
        val arguments = Bundle()
        viewModel.episode.value?.id?.let {
            arguments.putLong(EPISODE_ID, it)
        }
        fragment.arguments = arguments
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()

        // FIXME
        viewModel.episode.observe(this, Observer {
            fragment.viewModel.episodeId = it?.id ?: -1
            fragment.viewModel.onRefresh()
        })
    }

    private fun setupRecordEdit() {
        viewModel.shareTwitter.observe(this, Observer {
            viewModel.toggleShareTwitter()
        })

        viewModel.shareFacebook.observe(this, Observer {
            viewModel.toggleShareFacebook()
        })
    }

    override fun onClickBackArrow() {
        supportFinishAfterTransition()
    }

    override fun onRecorded() {
        supportFinishAfterTransition()
    }
}