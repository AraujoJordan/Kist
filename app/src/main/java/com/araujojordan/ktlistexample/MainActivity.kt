package com.araujojordan.ktlistexample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.araujojordan.ktlist.KtList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

    var ktListAdapter: KtList<String>? = null
    var index = 0

    var stringList = listOf<String>()

    var haveHeader: Boolean = false
    var haveFooter: Boolean = false
    var haveEmpty: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupktList()

        headerToggleButton?.setOnCheckedChangeListener { _, isChecked ->
            haveHeader = isChecked
            setupktList()
        }
        emptyToggleButton?.setOnCheckedChangeListener { _, isChecked ->
            haveEmpty = isChecked
            setupktList()
        }
        footerToggleButton?.setOnCheckedChangeListener { _, isChecked ->
            haveFooter = isChecked
            setupktList()
        }
        loadingToggleButton?.setOnCheckedChangeListener { _, isChecked ->
            ktListAdapter?.setLoading(isChecked)
        }
    }

    fun setupktList() {

        stringList = ktListAdapter?.getList()?:listOf()
        ktListAdapter = KtList(stringList, R.layout.item) { item, view -> view.item_text.text = item }

        if (haveHeader) ktListAdapter?.headerLayout = R.layout.header
        if (haveFooter) {
            ktListAdapter?.footerLayout = R.layout.end
            ktListAdapter?.footerModifier = { footer(it) }
        }
        ktListAdapter?.clickListener = { item, position -> click(item) }
        ktListAdapter?.endOfScroll = {
            endOfScroll()
        }
        if(haveEmpty) ktListAdapter?.emptyLayout = R.layout.empty

        recycleview.adapter = ktListAdapter
    }

    fun footer(it: View) {
        it.setOnClickListener {
            ktListAdapter?.setLoading(true)
            index++
            ktListAdapter?.addItems(listOf("RANDOM VALUE $index"))
            ktListAdapter?.setLoading(false)
        }
    }

    fun click(item: String) {
        ktListAdapter?.removeItems(item)
    }

    fun endOfScroll() {
        Toast.makeText(this, "End of scroll reached!", Toast.LENGTH_LONG).show()
        val moreElements = ArrayList<String>()
        for (i in 1..5) {
            moreElements.add("RANDOM VALUE FROM END OF SCROLL ${index + i}")
        }
        index += 5
        ktListAdapter?.addItems(moreElements)
    }
}
