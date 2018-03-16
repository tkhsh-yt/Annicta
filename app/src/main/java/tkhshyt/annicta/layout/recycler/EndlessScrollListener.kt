package tkhshyt.annicta.layout.recycler

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndlessScrollListener(val llm: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    protected val visibleThreshold = 5

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView?.childCount ?: 0
        val totalItemCount = llm.itemCount
        val firstVisibleItem = llm.findFirstVisibleItemPosition()

        if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore()
        }
    }

    abstract fun onLoadMore()
}