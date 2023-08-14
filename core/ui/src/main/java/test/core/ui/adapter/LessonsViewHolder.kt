package test.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import test.core.model.Lesson
import test.core.ui.R
import test.core.ui.databinding.ItemLessonBinding

class LessonsViewHolder(
    private val itemLessonBinding: ItemLessonBinding
) : ViewHolder(itemLessonBinding.root) {

    fun onBind(item: Lesson, onLessonClick: (Int, Boolean) -> Unit) {
        with(itemLessonBinding) {
            titleTv.text = item.name
            descriptionTv.text = item.description
            thumbnailIv.load(item.thumbnail) {
                crossfade(true)
            }

            if (item.isOpen) {
                transparentView.setBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        android.R.color.transparent
                    )
                )
            } else {
                transparentView.setBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.gray
                    )
                )
            }

            transparentView.setOnClickListener {
                onLessonClick(item.id, item.isOpen)
            }
        }
    }

    companion object {
        fun create(viewGroup: ViewGroup): LessonsViewHolder {
            return LessonsViewHolder(
                ItemLessonBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}