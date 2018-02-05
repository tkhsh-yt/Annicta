package tkhshyt.annicta.layout.recycler

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndlessScrollListener(val llm: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    protected var loading = false
    protected var nextPage = 1

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView?.childCount ?: 0
        val totalItemCount = llm.itemCount
        val firstVisibleItem = llm.findFirstVisibleItemPosition()

        if (!loading && canLoadMore()) {
            loading = true
            onLoadMore(nextPage)
        }
    }

    abstract fun onLoadMore(currentPage: Int)

    protected fun canLoadMore(): Boolean {
        return nextPage > 0
    }
}