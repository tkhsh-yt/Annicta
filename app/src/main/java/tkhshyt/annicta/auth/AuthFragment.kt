package tkhshyt.annicta.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tkhshyt.annicta.BaseFragment
import tkhshyt.annicta.R
import javax.inject.Inject

class AuthFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }
}