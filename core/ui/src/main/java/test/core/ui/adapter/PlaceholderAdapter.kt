package test.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import test.core.ui.R

class PlaceholderAdapter : Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val a = LayoutInflater.from(parent.context).inflate(
            R.layout.item_lesson_placeholder, parent, false
        )
        return VH(a)
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    inner class VH(view: View) : ViewHolder(view)
}