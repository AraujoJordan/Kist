package com.araujojordan.ktlistexample.oldExample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.araujojordan.ktlistexample.R

//class YourAdapter(var list: ArrayList<YoutItem>) : RecyclerView.Adapter<YourAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.yourItemLayout, parent, false)
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(list[position])
//    }
//
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//        var yourText = itemView.findViewById<TextView>(R.id.yourText)
//
//        fun bind(yourItem: YourItem) {
//            yourText.text = yourItem.text
//        }
//    }
//
//}