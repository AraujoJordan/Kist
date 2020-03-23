package com.araujojordan.ktlistexample

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.araujojordan.ktlist.KtList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

    var ktListAdapter: KtList<String>? = null
    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = ArrayList<String>()

        add.setOnClickListener {
            ktListAdapter?.setLoading(true)
            Handler().postDelayed({
                index++
                ktListAdapter?.addItems(listOf("RANDOM VALUE $index"))
                ktListAdapter?.setLoading(false)
            }, 250)
        }

        ktListAdapter = KtList(
            list,
            R.layout.item,
            headerLayout = R.layout.item,
            emptyLayout = R.layout.empty,
            footerLayout = R.layout.end,
            clickListener = { item, position ->
                ktListAdapter?.removeItems(item)
            },
            endOfScroll = {
//                Toast.makeText(this, "End of scroll reached!", Toast.LENGTH_LONG).show()
//                val moreElements = ArrayList<String>()
//                for (i in 1..100) {
//                    moreElements.add("RANDOM VALUEX ${index + i}")
//                }
//                index += 100
//                ktListAdapter?.addItems(moreElements)
            }
        ) { item, view ->
            view.item_text.text = item
        }

        recycleview.adapter = ktListAdapter

        ktListAdapter?.setLoading(true)
        Handler().postDelayed(Runnable { ktListAdapter?.setLoading(false) }, 2000)
    }
}
