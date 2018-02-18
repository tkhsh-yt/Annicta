package tkhshyt.annicta.layout.spinner

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner

class NoDefaultSpinner : Spinner {

    private var prevPos = -1

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun setSelection(position: Int) {
        super.setSelection(position)
        if (position == selectedItemPosition && prevPos == position) {
            onItemSelectedListener!!.onItemSelected(null, null, position, 0)
        }
        prevPos = position
    }
}