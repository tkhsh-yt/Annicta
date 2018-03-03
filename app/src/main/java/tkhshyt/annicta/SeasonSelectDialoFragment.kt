package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.Season
import tkhshyt.annicta.event.SeasonSelectedEvent

class SeasonSelectDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val season = arguments?.getSerializable("season") as Season
        val items = (season.year + 1 downTo 1960).flatMap { y ->
            val seasons = Season.Type.values().map {
                Season(y, it)
            }
            seasons.asIterable()
        }
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, items)

        val builder = context?.let {
            AlertDialog.Builder(it)
                .setAdapter(adapter, { _, which ->
                    EventBus.getDefault().post(SeasonSelectedEvent(adapter.getItem(which)))
                })
        }
        return builder!!.create()
    }
}
