package com.araujojordan.kist.recycleviewLayoutManagers

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * Much safer LayoutManager than the original one
 */
class SupportLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context?, stableId: Boolean = false) : super(context) {
        isItemPrefetchEnabled = stableId
    }

    constructor(
        context: Context?,
        orientation: Int,
        reverseLayout: Boolean,
        stableId: Boolean = false
    ) :
            super(context, orientation, reverseLayout) {
        isItemPrefetchEnabled = stableId
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        stableId: Boolean = false
    ) :
            super(context, attrs, defStyleAttr, defStyleRes) {
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