package com.notepad.Notepad.Adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.notepad.Notepad.Data.Models.MarkBoolean
import com.notepad.Notepad.R
import com.notepad.Notepad.RX.RxMark
import com.notepad.Notepad.Retrofit.Common.Common
import com.notepad.Notepad.Retrofit.Data.Success
import com.notepad.Notepad.Retrofit.Data.Tasks
import com.notepad.Notepad.Retrofit.RetrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasksAdapter (private val context: Context, private val movieList: MutableList<Tasks>):
    RecyclerView.Adapter<TasksAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txt_name: TextView = itemView.findViewById(R.id.txt_name)
        val txt_text: TextView = itemView.findViewById(R.id.txt_text)
        val date: TextView = itemView.findViewById(R.id.date)
        val complete: TextView = itemView.findViewById(R.id.complete)
        var mService: RetrofitApi = Common.retrofitApi

        @SuppressLint("ResourceAsColor")
        fun bind(listItem: Tasks, context: Context) {
            if(listItem.status!=null){
                complete.setBackgroundColor(R.color.colorTheme)
                complete.setText(R.string.complete)
            }else{
                complete.setBackgroundColor(R.color.white)
                complete.setText(R.string.noComplete)
            }

            itemView.setOnClickListener {
                if(listItem.status==null){
                    val snackBar = Snackbar.make(it, R.string.task_is_complete,
                        Snackbar.LENGTH_LONG).setAction(R.string.yes) {
                        listItem.id?.let { it1 ->
                            mService.completeTask(it1).enqueue(object : Callback<Success> {
                                override fun onFailure(call: Call<Success>, t: Throwable) {
                                    Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
                                }
                                override fun onResponse(
                                    call: Call<Success>,
                                    response: Response<Success>) {
                                    RxMark.getInstance()?.publishBoolean(MarkBoolean(true))
                                    Toast.makeText(context, R.string.success, Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                    snackBar.setActionTextColor(Color.BLUE)
                    val snackBarView = snackBar.view
                    snackBarView.setBackgroundColor(Color.LTGRAY)
                    val textView =  snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.BLUE)
                    textView.textSize = 28f
                    snackBar.show()
                }else{
                    Toast.makeText(context, R.string.task_is_completed, Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = movieList[position]
        holder.bind(listItem,context)
        holder.txt_name.text = movieList[position].name
        holder.txt_text.text = movieList[position].text
        holder.date.text = movieList[position].date.toString()
    }

}