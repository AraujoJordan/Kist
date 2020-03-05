package com.araujojordan.ktlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.araujojordan.ktlist.recycleviewLayoutManagers.SupportGridLayoutManager
import com.araujojordan.ktlist.recycleviewLayoutManagers.SupportLinearLayoutManager

/**
 * Designed and developed by Jordan Lira (@araujojordan)
 *
 * Copyright (C) 2020 Jordan Lira de Araujo Junior
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * KtList is a RecyclerView.Adapter implementation that make easier to implement hard stuffs like
 * HeaderView, EmptyView, InfiniteScroll and so on. It will also make it easy to implement the
 * adapter itself as you don't need to implement ViewHolders and others boilerplate methods won't
 * change in most of implementations.
 *
 * @author Jordan Lira de Araujo Junior (araujojordan)
 * @param list List that you will be show (ex: ArrayList, LinkedList...)
 * @param layout Item Int layout resource reference (ex: R.layout.item_view)
 * @param layoutManager (Optional) The type of layout, if you don't put it, ill will be LinearLayout
 * @param headerLayout (Optional) Header Int layout resource reference (ex: R.layout.list_header)
 * @param headerModifier (Optional) If you want to modifier your header elements, use this param
 * @param footerLayout (Optional) Footer Int layout resource reference (ex: R.layout.list_footer)
 * @param footerModifier (Optional) If you want to modifier your footer elements, use this param
 * @param emptyView (Optional) If you want to show something if the list is empty (it will always respect the header view)
 * @param endOfScroll (Optional) If you want to implement infinite scrolling, implement this lambda
 * @param clickListener (Optional) If you want to implement click action in the entire list item, implement this lambda
 * @param longClickListener (Optional) If you want to implement long click in the entire list item, implement this lambda
 * @param binding (Optional) If you want to implement infinite scrolling, implement this lambda
 */
class KtList<T>(
    private var list: List<T>,
    private var layout: Int,
    private var layoutManager: RecyclerView.LayoutManager? = null,
    private var headerLayout: Int? = null,
    private var headerModifier: ((headerView: View) -> Unit)? = null,
    private var footerLayout: Int? = null,
    private var footerModifier: ((footerView: View) -> Unit)? = null,
    var emptyLayout: Int? = null,
    private var endOfScroll: (() -> Unit)? = null,
    private var clickListener: ((item: T, position: Int) -> Unit)? = null,
    private var longClickListener: ((item: T, position: Int) -> Unit)? = null,
    private var binding: (T, itemView: View) -> Unit
) : RecyclerView.Adapter<KtList<T>.ViewHolder>() {

    enum class TYPE {
        HEADER,
        ITEM,
        EMPTY,
        FOOTER
    }

    //Copy the list so the outside list changes will not affect this one
    init {
        list = list.toMutableList()
    }

    //Variable used if the user activate the endOfScroll variable
    val endOfScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                val visibleItemCount = recyclerView.layoutManager?.childCount ?: 0
                val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                val pastVisibleItems = when (recyclerView.layoutManager) {
                    is LinearLayoutManager -> (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    is GridLayoutManager -> (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                    is SupportLinearLayoutManager -> (recyclerView.layoutManager as SupportLinearLayoutManager).findFirstVisibleItemPosition()
                    is SupportGridLayoutManager -> (recyclerView.layoutManager as SupportGridLayoutManager).findFirstVisibleItemPosition()
                    else -> 3
                }
                if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                    endOfScroll?.invoke()
                }
            }
        }
    }

    /**
     * This add come features that can be optional,
     * The first one is a fallback to prevent the creation of a RecycleView without an
     * LayoutManager using the most used (LinearLayoutManager)
     * The second is for the optional argument endOfScroll(), if exist, it will overwrite the
     * RecyclerView.OnScrollListener, so it will detect the end of the scroll and call the lambda
     * to let the user know when to add more elements in the list (infinite list implementation)
     * Check the endOfScrollListener() variable bellow for more
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.layoutManager == null && layoutManager == null)
            recyclerView.layoutManager = SupportLinearLayoutManager(recyclerView.context)
        if (layoutManager != null) recyclerView.layoutManager = layoutManager

        if (endOfScroll != null) recyclerView.addOnScrollListener(endOfScrollListener)
    }

    fun getItemByIndex(index: Int) = list[index]

    fun first(searchFor:(item:T) -> Boolean) = list.firstOrNull { searchFor(it) }
    fun filter(searchFor:(item:T) -> Boolean) = list.filter { searchFor(it) }


    /**
     * Simple function to count the existence of the header
     */
    private fun countHeader() = if (headerLayout != null) 1 else 0

    /**
     * Simple function to count the existence of the footer
     */
    private fun countFooter() = if (footerLayout != null) 1 else 0

    /**
     * Simple function to count the existence of the empty view
     */
    private fun countEmpty() = if (emptyLayout != null && list.isEmpty()) 1 else 0

    /**
     * Use this method to add more elements to the list, it will also handle if you already have
     * some elements and just want add more. This will also prevent crashes of
     * IndexOutOfBoundsException of the validateViewHolderForOffsetPosition() method.
     * WARNING: NEVER CHANGE THE LIST DIRECTLY, CHANGE THE REFERENCE!
     *
     * See more at: https://medium.com/@nhancv/android-fix-java-lang-indexoutofboundsexception-inconsistency-detected-invalid-item-70e9b3b489a2
     *
     * @param itemsToAdd the items that will be added to the displayed list
     */
    fun addItems(itemsToAdd: List<T>) {
        val newList = ArrayList<T>()
        newList.addAll(list)
        newList.addAll(itemsToAdd)
        list = newList
        notifyDataSetChanged()
    }


    /**
     * Use this method to remove one or more elements to the list, it will also prevent crashes of
     * IndexOutOfBoundsException of the validateViewHolderForOffsetPosition() method.
     * WARNING: NEVER CHANGE THE LIST DIRECTLY, CHANGE THE REFERENCE!
     *
     * See more at: https://medium.com/@nhancv/android-fix-java-lang-indexoutofboundsexception-inconsistency-detected-invalid-item-70e9b3b489a2
     *
     * @param itemsToRemove the elements that will be removed from the list
     */
    fun removeItems(vararg itemsToRemove: T) {
        val newList = list.toMutableList()
        newList.removeAll(itemsToRemove)
        list = newList
        notifyDataSetChanged()
    }

    /**
     * Use this method to remove one or more elements to the list, it will also prevent crashes of
     * IndexOutOfBoundsException of the validateViewHolderForOffsetPosition() method.
     * WARNING: NEVER CHANGE THE LIST DIRECTLY, CHANGE THE REFERENCE!
     *
     * See more at: https://medium.com/@nhancv/android-fix-java-lang-indexoutofboundsexception-inconsistency-detected-invalid-item-70e9b3b489a2
     *
     * @param indexesToRemove the index(es) that will be removed from the list
     */
    fun removeItemsIndex(vararg indexesToRemove: Int) {
        val newList = list.toMutableList()
        indexesToRemove.forEach { newList.removeAt(it) }
        list = newList
        notifyDataSetChanged()
    }

    /**
     * Safely change list from KtList
     */
    fun setList(newList: List<T>) {
        this.list = ArrayList(newList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            emptyLayout != null && list.isEmpty() && position == countHeader() -> TYPE.EMPTY.ordinal
            headerLayout != null && position == 0 -> TYPE.HEADER.ordinal
            footerLayout != null && position == list.size + countEmpty() + countHeader() -> TYPE.FOOTER.ordinal
            else -> TYPE.ITEM.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            TYPE.HEADER.ordinal ->
                HeaderHolder(LayoutInflater.from(parent.context), parent)
            TYPE.ITEM.ordinal ->
                ItemHolder(LayoutInflater.from(parent.context), parent)
            TYPE.EMPTY.ordinal ->
                EmptyHolder(LayoutInflater.from(parent.context), parent)
            TYPE.FOOTER.ordinal ->
                FooterHolder(LayoutInflater.from(parent.context), parent)
            else -> ItemHolder(LayoutInflater.from(parent.context), parent)
        }

    override fun getItemCount() =
        countHeader() + countEmpty() + list.size + countFooter()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            when (holder) {
                is ItemHolder -> holder.bind(list[position - countHeader()])
                is HeaderHolder -> holder.bind()
                is FooterHolder -> holder.bind()
                is EmptyHolder -> {
                } //Do you really need to change an empty view?
            }
        } catch (err: Exception) {
        }
    }


    @Suppress("UNCHECKED_CAST")
    val clickListenerAction = View.OnClickListener { view ->
        try {
            val itemElement = view?.tag as T
            val index = list.indexOfFirst { itemElement == it }
            clickListener?.invoke(itemElement, index)
        } catch (err: Exception) {
        }
    }

    @Suppress("UNCHECKED_CAST")
    val longClickListenerAction = View.OnLongClickListener { view ->
        try {
            val itemElement = view?.tag as T
            val index = list.indexOfFirst { itemElement == it }
            longClickListener?.invoke(itemElement, index)
            true
        } catch (err: Exception) {
            false
        }
    }

    open inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup, holderLayout: Int) :
        RecyclerView.ViewHolder(inflater.inflate(holderLayout, parent, false)) {
        fun bind(item: T) {
            if (clickListener != null || longClickListener != null) {
                itemView.tag = item
                if (clickListener != null)
                    itemView.setOnClickListener(clickListenerAction)
                else
                    itemView.setOnClickListener(null)
                if (longClickListener != null)
                    itemView.setOnLongClickListener(longClickListenerAction)
                else
                    itemView.setOnLongClickListener(null)
            }
            binding(item, itemView)
        }
    }

    inner class ItemHolder(inflater: LayoutInflater, parent: ViewGroup) :
        ViewHolder(inflater, parent, layout)

    inner class EmptyHolder(inflater: LayoutInflater, parent: ViewGroup) :
        ViewHolder(inflater, parent, emptyLayout ?: layout)

    inner class HeaderHolder(inflater: LayoutInflater, parent: ViewGroup) :
        ViewHolder(inflater, parent, headerLayout ?: layout) {
        fun bind() = headerModifier?.let {
            it(itemView)
            notifyItemChanged(0)
        }
    }

    inner class FooterHolder(inflater: LayoutInflater, parent: ViewGroup) :
        ViewHolder(inflater, parent, footerLayout ?: layout) {
        fun bind() = footerModifier?.let {
            it(itemView)
            notifyItemChanged(itemCount)
        }
    }
}