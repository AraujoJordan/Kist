package com.araujojordan.kist

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.araujojordan.kist.recycleviewLayoutManagers.SupportGridLayoutManager
import com.araujojordan.kist.recycleviewLayoutManagers.SupportLinearLayoutManager

class Kist(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    private var layoutItem: Int = 0
    private var headerLayout: Int? = null
    private var footerLayout: Int? = null
    private var loadingLayout: Int? = null
    private var emptyLayout: Int? = null
    var bindLayout: ((item: Any, view: View) -> Unit)? = null
    var bindHeader: ((headerView: View) -> Unit)? = null
    var bindFooter: ((headerView: View) -> Unit)? = null
    var bindLoader: ((footerView: View) -> Unit)? = null
    var onEndOfScroll: (() -> Unit)? = null
    var onClickListener: ((item: Any, position: Int, view: View) -> Unit)? = null
    var onLongClickListener: ((item: Any, position: Int, view: View) -> Unit)? = null
    private var isGrid: Boolean = false
    private var spanCount: Int = 2
    var listWithLoading : Boolean = false

    private var adapter: KistAdapter<Any>? = null

    private val layoutManager by lazy {
        if (isGrid) SupportGridLayoutManager(context, spanCount) else SupportLinearLayoutManager(
            context
        )
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.kist, 0, 0).apply {
            layoutItem = getResourceId(R.styleable.kist_itemLayout, 0)
            var layout = getResourceId(R.styleable.kist_headerLayout, 0)
            if (layout != 0) headerLayout = layout
            layout = getResourceId(R.styleable.kist_footerLayout, 0)
            if (layout != 0) footerLayout = layout
            layout = getResourceId(R.styleable.kist_emptyLayout, 0)
            if (layout != 0) emptyLayout = layout
            layout = getResourceId(R.styleable.kist_loadingLayout, 0)
            if (layout != 0) loadingLayout = layout
            spanCount = getInteger(R.styleable.kist_spanCount, 2)
            isGrid = getBoolean(R.styleable.kist_isGrid, false)
        }
    }

    fun add(vararg items: Any) {
        if (adapter == null) {
            adapter = KistAdapter(
                list = items.toList(),
                layout = layoutItem,
                headerLayout = headerLayout,
                headerModifier = bindHeader,
                footerLayout = footerLayout,
                footerModifier = bindFooter,
                listWithLoading = listWithLoading,
                loadingView = loadingLayout,
                loadingModifier = bindLoader,
                emptyLayout = emptyLayout,
                clickListener = onClickListener,
                longClickListener = onLongClickListener,
                layoutManager = layoutManager,
                endOfScroll = onEndOfScroll,
                binding = bindLayout,
            )
            setAdapter(adapter)
        } else
            adapter?.addItems(items.toList())
    }

    fun setList(list: List<Any>) {
        if (adapter == null) {
            adapter = KistAdapter(
                list = list,
                layout = layoutItem,
                headerLayout = headerLayout,
                headerModifier = bindHeader,
                footerLayout = footerLayout,
                footerModifier = bindFooter,
                listWithLoading = listWithLoading,
                loadingView = loadingLayout,
                loadingModifier = bindLoader,
                emptyLayout = emptyLayout,
                clickListener = onClickListener,
                longClickListener = onLongClickListener,
                layoutManager = layoutManager,
                endOfScroll = onEndOfScroll,
                binding = bindLayout,
            )
            setAdapter(adapter)
        } else
            adapter?.setList(list)
    }

    fun remove(position: Int) = adapter?.removeItemsIndex(position)

    fun remove(vararg itemsToRemove: Any) = adapter?.removeItems(*itemsToRemove)

    fun setLoading(isLoading: Boolean) = adapter?.setLoading(isLoading)

    @Deprecated("Use .size get method instead")
    fun listSize() = adapter?.itemCount ?: 0

    val size get() = adapter?.itemCount ?: 0

    val isEmpty get() = adapter?.itemCount ?: 0 == 0

    fun updateLine(position: Int) = adapter?.updateLine(position)

}