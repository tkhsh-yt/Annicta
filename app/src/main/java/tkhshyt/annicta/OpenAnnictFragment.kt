package tkhshyt.annicta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_open_annict.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.AnnictClient
import tkhshyt.annicta.event.OpenAnnictEvent

class OpenAnnictFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_open_annict, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openAnnictButton.setOnClickListener {
            openAuthorizeUrlExternal()
        }

        haveCodeButton.setOnClickListener {
            EventBus.getDefault().post(OpenAnnictEvent())
        }
    }

    private fun openAuthorizeUrlExternal() {
        if(arguments.containsKey("client_id")) {
            val uri = Uri.parse(AnnictClient.authorizeUrl(
                    arguments.getString("client_id"))
            )
            val intent = Intent(Intent.ACTION_VIEW, uri)

            startActivity(intent)

            EventBus.getDefault().post(OpenAnnictEvent())
        }
    }
}