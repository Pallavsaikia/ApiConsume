package com.pallavsaikia.task.utilities

import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object PaginationUtils {
    private var isScrolling = false
    private var page = 1

    fun initPagination(mRecycler: RecyclerView, mLayoutManager: LinearLayoutManager,
                       pageListener: PageListener) {
        mRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
                if(page==1){
                    isScrolling = false
                    pageListener.onPagination(++page)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentItems = mLayoutManager.childCount
                val totalItems = mLayoutManager.itemCount
                val scrolledOutItems = mLayoutManager.findFirstVisibleItemPosition()

                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    isScrolling = false
                    pageListener.onPagination(++page)
                }
            }
        })
    }
}