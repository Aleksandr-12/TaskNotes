package com.notepad.Notepad.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.notepad.Notepad.Activity.ActivityNote
import com.notepad.Notepad.Data.Models.Note
import com.notepad.Notepad.R
import com.notepad.Notepad.Fragments.note.NoteViewModel
import java.text.SimpleDateFormat

class NoteAdapter(private val context: Context, private  val noteViewModel: NoteViewModel)  : ListAdapter<Note, NoteAdapter.NoteViewHolder>(
    NotesComparator()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current,context,noteViewModel)
    }
    
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageFavorite: ImageView = itemView.findViewById(R.id.imageFavorite)
        private val item: LinearLayout = itemView.findViewById(R.id.item)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val noteItem: TextView = itemView.findViewById(R.id.note)
        private val date: TextView = itemView.findViewById(R.id.date)
        @SuppressLint("SimpleDateFormat")
        var format = SimpleDateFormat("dd.MM.yyyy HH:mm")
        fun bind(note: Note?, context: Context, noteViewModel: NoteViewModel?) {
            title.text = note!!.title
            noteItem.text = note.note
            date.text = format.format(note.date)
            item.setOnClickListener {
                val intent = Intent(context, ActivityNote::class.java)
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("note", note)
                context.startActivity(intent)
            }
            if(note.favorite !=null) {
                if (note.favorite!!) {
                    imageFavorite.setImageResource(R.mipmap.favorite_foreground)
                } else {
                    imageFavorite.setImageResource(R.mipmap.no_favorite_foreground)
                }
            }else{
                imageFavorite.setImageResource(R.mipmap.no_favorite_foreground)
            }
            imageFavorite.setOnClickListener {
                if(note.favorite !=null){
                    val favorite = if (note.favorite!!) {
                        imageFavorite.setImageResource(R.mipmap.no_favorite_foreground)
                        false
                    }else {
                        imageFavorite.setImageResource(R.mipmap.favorite_foreground)
                        true
                    }
                    noteViewModel!!.updateFavorite(note.id,favorite)
                }else{
                    noteViewModel!!.updateFavorite(note.id,true)
                    imageFavorite.setImageResource(R.mipmap.favorite_foreground)
                }

            }
        }
        companion object {
            fun create(parent: ViewGroup): NoteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return NoteViewHolder(view)
            }
        }
    }

    class NotesComparator : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.date == newItem.date
        }
    }
}