package com.legec.ceburilo

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.legec.ceburilo.web.veturilo.VeturiloPlace


class PointListAdapter(context: Context, textViewResourceId: Int, val points: List<VeturiloPlace>)
    : ArrayAdapter<VeturiloPlace>(context, textViewResourceId, points) {

    override fun getCount(): Int {
        return points.size
    }

    override fun getItem(position: Int): VeturiloPlace {
        return points[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val label = TextView(context)
        label.setTextColor(Color.BLACK)
        label.text = points[position].name
        return label
    }
}