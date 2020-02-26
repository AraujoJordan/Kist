package com.araujojordan.ktlist.recycleviewLayoutManagers

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * Much safer LayoutManager than the original one
 */
class SupportGridLayoutManager : GridLayoutManager {

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
    constructor(context: Context?, spanCount: Int) : super(context, spanCount)
    constructor(
        context: Context?,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout)

    override fun onLayoutChildren(
        recycler: Recycler,
        state: RecyclerView.State
    ) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("SupportLinearLM",
                "Problem to load item on list"
            )
        }
    }

    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}