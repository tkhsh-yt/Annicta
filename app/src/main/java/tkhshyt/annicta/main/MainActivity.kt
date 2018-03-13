package tkhshyt.annicta.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import tkhshyt.annicta.BaseActivity
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ActivityMainBinding
import tkhshyt.annicta.di.ViewModelModule
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        getInjector().viewModelComponent(ViewModelModule())
            .inject(this)

        val binding = DataBindingUtil
            .setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        setupToolbar()

        viewModel.onStart()
    }

    fun setupToolbar() {
        setSupportActionBar(toolbar)
    }
}