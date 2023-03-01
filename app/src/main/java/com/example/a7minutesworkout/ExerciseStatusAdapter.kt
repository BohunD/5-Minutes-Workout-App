package com.example.a7minutesworkout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>):
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {
        class ViewHolder(binding: ItemExerciseStatusBinding):
            RecyclerView.ViewHolder(binding.root){
            val tvItem = binding.tvItem
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = items[position]
        holder.tvItem.text = model.getId().toString()
        when{
            model.getIsSelected()->{
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.circular_white_background)
                holder.tvItem.setTextColor(R.color.black)
            }
            model.getIsCompleted()->{
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.item_circular_color_accent_background)
                holder.tvItem.setTextColor(R.color.black)
            }
            else->{
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.circular_gray_background)
                holder.tvItem.setTextColor(R.color.black)

            }
        }

    }
}