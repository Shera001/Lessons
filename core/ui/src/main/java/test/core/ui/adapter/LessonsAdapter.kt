package test.core.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import test.core.model.Lesson

class LessonsAdapter(
    private val onLessonClick: (Int, Boolean) -> Unit
) : ListAdapter<Lesson, LessonsViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        return LessonsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        holder.onBind(getItem(position), onLessonClick)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }
    }
}