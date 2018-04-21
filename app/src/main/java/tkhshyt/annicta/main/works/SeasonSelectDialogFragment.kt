package tkhshyt.annicta.main.works

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.ArrayAdapter
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.Season
import tkhshyt.annicta.event.SeasonSelectedEvent
import java.util.*

class SeasonSelectDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = Season.current().year
        val items = ((year+1) downTo 1960).flatMap { y ->
            val seasons = Season.Type.values().map {
                Season(y, it)
            }
            seasons.asIterable()
        }
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, items)

        val builder = context?.let {
            AlertDialog.Builder(it)
                .setAdapter(adapter, {_, which ->
                    EventBus.getDefault().post(SeasonSelectedEvent(adapter.getItem(which)))
                })
        }

        return builder!!.create()
    }
}