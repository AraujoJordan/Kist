package com.araujojordan.ktlistexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.araujojordan.ktlist.KtList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = ArrayList<String>()
        list.add("1")
        list.add("2")
        list.add("3")

        recycleview.adapter = KtList(
            list,
            R.layout.item
        ) { item, view ->
            view.item_text.text = item
        }
    }
}
