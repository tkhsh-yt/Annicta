package tkhshyt.annicta.layout.spinner

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import tkhshyt.annicta.R

class RatingAdapter(context: Context?, objects: Array<out String>?) : ArrayAdapter<String>(context, R.layout.item_status, objects) {

    var selectedItem = 0

    init {
        setDropDownViewResource(R.layout.item_status_dropdown)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View? = convertView ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.item_status, null)
        view?.findViewById<TextView>(R.id.status)?.text = getItem(position)

        return view!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = super.getDropDownView(position, convertView, parent)
        if(position == selectedItem) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_700))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_800))
        }
        return view
    }
}