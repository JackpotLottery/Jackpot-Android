package com.nickbryanmiller.jackpotlottery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.*


class CustomAdapter : BaseAdapter {
    private val mContext: Context
    private val mDataSource: ArrayList<GroupDataObject>

    constructor(mContext: Context, mDataSource: ArrayList<GroupDataObject>) : super() {
        this.mContext = mContext
        this.mDataSource = mDataSource
        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    private val mInflater: LayoutInflater


    override fun getCount(): Int {
        return mDataSource.size
    }

    override fun getItem(position: Int): Any {
        return mDataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var myConvertView = convertView

        val holder: ViewHolder

        // check if the view already exists if so, no need to inflate and findViewById again!
        if (myConvertView == null) {

            myConvertView = mInflater.inflate(R.layout.group_view_row, parent, false)

            // create a new "Holder" with subviews
            holder = ViewHolder()
//            holder.thumbnailImageView = convertView!!.findViewById<ImageView>(R.id.recipe_list_thumbnail) as ImageView
            holder.groupNameTextView = myConvertView?.findViewById<TextView>(R.id.group_name_textView)
            holder.groupDescriptionTextView = myConvertView.findViewById<TextView>(R.id.group_description_textView)

            // hang onto this holder for future recycling
            myConvertView.tag = holder
        } else {

            // skip all the expensive inflation/findViewById and just get the holder you already made
            holder = myConvertView.tag as ViewHolder
        }

        val group = getItem(position) as GroupDataObject

        val groupNameTextView = holder.groupNameTextView
        groupNameTextView?.text = group.name
        val groupDescriptionTextView = holder.groupDescriptionTextView
        groupDescriptionTextView?.text = group.description
//        val thumbnailImageView = holder.thumbnailImageView

        return myConvertView!!
    }

    private class ViewHolder {
        var groupNameTextView: TextView? = null
        var groupDescriptionTextView: TextView? = null
//        var thumbnailImageView: ImageView? = null
    }
}