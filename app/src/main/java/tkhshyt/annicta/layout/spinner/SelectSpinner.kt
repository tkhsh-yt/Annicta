package tkhshyt.annicta.layout.spinner

import android.content.Context
import android.support.v7.widget.AppCompatSpinner
import android.widget.AdapterView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SelectSpinner : AppCompatSpinner, AdapterView.OnItemSelectedListener {

    internal var spinnerTouched = false
    internal var onItemClickListener: OnItemClickListener? = null
    internal var onItemSelectedListener: OnItemSelectedListener? = null

    constructor(context: Context) : super(context) {
        super.setOnItemSelectedListener(this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        super.setOnItemSelectedListener(this)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        super.setOnItemSelectedListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        spinnerTouched = true
        return super.onTouchEvent(event)
    }

    override fun setOnItemClickListener(listener: OnItemClickListener?) {
        onItemClickListener = listener
    }

    override fun setOnItemSelectedListener(onItemSelectedListener: OnItemSelectedListener?) {
        this.onItemSelectedListener = onItemSelectedListener
        super.setOnItemSelectedListener(this)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (spinnerTouched && this.onItemClickListener != null) this.onItemClickListener!!.onItemClick(parent, view, position, id)
        if (this.onItemSelectedListener != null) this.onItemSelectedListener!!.onItemSelected(parent, view, position, id)
        spinnerTouched = false
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        if (this.onItemSelectedListener != null) this.onItemSelectedListener!!.onNothingSelected(parent)
        spinnerTouched = false
    }
}