package com.araujojordan.kistexample

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.araujojordan.kist.KistAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.empty.view.*

class MainActivity : AppCompatActivity() {

    var KistAdapterAdapter: KistAdapter<String>? = null
    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = ArrayList<String>()

        addButton.setOnClickListener {
            KistAdapterAdapter?.setLoading(true)
            Handler().postDelayed({
                index++
                KistAdapterAdapter?.addItems(listOf("RANDOM VALUE $index"))
                KistAdapterAdapter?.setLoading(false)
            }, 250)
        }

        KistAdapterAdapter = KistAdapter(
            list,
            R.layout.item,
            headerLayout = R.layout.item,
            footerLayout = R.layout.end,
            emptyLayout = R.layout.empty,
            endOfScroll = {
                Toast.makeText(this, "End of scroll reached!", Toast.LENGTH_LONG).show()
            },
            clickListener = { item, position, view ->
                KistAdapterAdapter?.removeItems(item)
            }
        ) { item, view ->
            view.item_text.text = item
        }

        recycleview.adapter = KistAdapterAdapter

        KistAdapterAdapter?.setLoading(true)
        Handler().postDelayed(Runnable { KistAdapterAdapter?.setLoading(false) }, 2000)
    }
}
