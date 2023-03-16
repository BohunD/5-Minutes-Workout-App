package com.example.a7minutesworkout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.HistoryItemsBinding

class HistoryAdapter(private val items: ArrayList<HistoryEntity>,
                     private val deleteListener: (id:Int)->Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: HistoryItemsBinding): RecyclerView.ViewHolder(binding.root){
        val tvDate = binding.tvDate
        val llMain = binding.llMain
        val ivDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HistoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvDate.text = item.date


        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id) }
    }


}