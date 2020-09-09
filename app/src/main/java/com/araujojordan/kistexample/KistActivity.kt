package com.araujojordan.kistexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_kist.*
import kotlinx.android.synthetic.main.item.view.*

class KistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kist)

        kistView.bindLayout = { item, view -> view.item_element.text = item.toString()}
        kistView.onClickListener = { item, position, view ->  kistView.remove(position) }
        kistView.onEndOfScroll = { addMore() }
        kistView.add(1, 2, 3, 4, 5)

        addKistButton.setOnClickListener { addMore() }
    }

    fun addMore() = kistView.add(kistView.size-1)
}
