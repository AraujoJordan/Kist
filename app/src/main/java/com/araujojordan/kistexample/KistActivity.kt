package com.araujojordan.kistexample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_kist.*
import kotlinx.android.synthetic.main.item.view.*

class KistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kist)

        kistView.bindLayout = { item, view -> view.item_element.text = item as String }
        kistView.onClickListener = { item, position, view ->  kistView.remove(position) }
        kistView.onEndOfScroll = { kistView.add("more") }

        kistView.add("One", "Two", "Three", "Four", "Five")

    }

    fun addMore(view: View) {
        println("Add")
        kistView.add("YOLOOO")
    }
}
