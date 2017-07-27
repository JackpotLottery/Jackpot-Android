package com.nickbryanmiller.jackpotlottery

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

class MyEventRecyclerViewAdapter(private val mDataset: ArrayList<EventDataObject>) : RecyclerView.Adapter<MyEventRecyclerViewAdapter.DataObjectHolder>() {

    private var myClickListener: MyClickListener? = null

    class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var eventNameTextView: TextView = itemView.findViewById<TextView>(R.id.event_name_textView)
        internal var creatorTextView: TextView = itemView.findViewById<TextView>(R.id.event_group_name_textView)
        internal var dateTimeTextView: TextView = itemView.findViewById<TextView>(R.id.event_date_time_textView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            myClickListener?.onItemClick(adapterPosition, v)
        }
    }

    fun setOnItemClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_row, parent, false)
        val dataObjectHolder = DataObjectHolder(view)
        return dataObjectHolder
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.eventNameTextView.text = mDataset[position].name
        holder.dateTimeTextView.text = mDataset[position].date

        for (group in User.sharedInstance!!.getAllGroups()) {
            if (group.id == mDataset[position].groupID) {
                holder.creatorTextView.text = "Hosted by: " + group.name
                break
            }
        }
    }

    fun addItem(eventDataObj: EventDataObject, index: Int) {
        mDataset.add(index, eventDataObj)
        notifyItemInserted(index)
    }

    fun deleteItem(index: Int) {
        mDataset.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    interface MyClickListener {
        fun onItemClick(position: Int, v: View)
    }

    companion object {
        private var myClickListener: MyClickListener? = null
    }
}
