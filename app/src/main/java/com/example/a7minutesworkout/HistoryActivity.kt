package com.example.a7minutesworkout

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

private var binding: ActivityHistoryBinding? = null
class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.historyToolbar)
        if(supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "History"
        }
        binding?.historyToolbar?.setNavigationOnClickListener { onBackPressed() }
        val historyDao = (application as HistoryApp).db.historyDao()
        lifecycleScope.launch{
            historyDao.fetchAllRecords().collect{
                val list = ArrayList(it)
                list.reverse()
                setupListOfDataIntoRecyclerView(list, historyDao)
            }
        }

    }
    private fun setupListOfDataIntoRecyclerView(historyList: ArrayList<HistoryEntity>,
    historyDao: HistoryDao){
        if(historyList.isNotEmpty()){
            val historyAdapter = HistoryAdapter(historyList) { deleteId ->
                deleteDialogWindow(
                    deleteId,
                    historyDao
                )
            }
            binding?.rvHistory?.layoutManager = LinearLayoutManager(this)
            binding?.rvHistory?.adapter = historyAdapter
        }
        else{
            binding?.rvHistory?.visibility = View.GONE
        }

    }

    private fun deleteDialogWindow(id: Int, historyDao: HistoryDao){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete?")
        builder.setNegativeButton("No"){
            dialogInterface, _ -> dialogInterface.dismiss()
        }
        builder.setPositiveButton("Yes"){
            dialogInterface, _ -> lifecycleScope.launch {
            if (this != null) {
                historyDao.delete(HistoryEntity(id))
            }
        }
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}