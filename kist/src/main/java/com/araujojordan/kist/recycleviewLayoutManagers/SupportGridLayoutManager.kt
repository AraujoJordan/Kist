package com.araujojordan.kist.recycleviewLayoutManagers

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.araujojordan.kist.R

/**
 * Much safer LayoutManager than the original one
 */
class SupportGridLayoutManager : GridLayoutManager {

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        stableId: Boolean = false
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        isItemPrefetchEnabled = stableId
    }

    constructor(context: Context?, spanCount: Int, stableId: Boolean = false) : super(
        context, context?.resources?.getInteger(R.integer.ktlist_grid_rows) ?: spanCount
    ) {
        isItemPrefetchEnabled = stableId
    }

    constructor(
        context: Context?,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean,
        stableId: Boolean = false
    ) : super(
        context,
        context?.resources?.getInteger(R.integer.ktlist_grid_rows) ?: spanCount,
        orientation,
        reverseLayout
    ) {
        isItemPrefetchEnabled = stableId
    }

    override fun onLayoutChildren(
        recycler: Recycler,
        state: RecyclerView.State
    ) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    override fun supportsPredictiveItemAnimations() = false
}