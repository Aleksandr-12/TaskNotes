package com.notepad.Notepad.Activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.notepad.Notepad.Data.Models.Note
import com.notepad.Notepad.Data.Repository.NoteRepository
import com.notepad.Notepad.NoteApplication
import com.notepad.Notepad.R
import com.notepad.Notepad.databinding.ActivityNoteBinding
import com.notepad.Notepad.Fragments.note.NoteViewModel
import com.notepad.Notepad.Fragments.note.NoteViewModelFactory
import java.util.*

class ActivityNote : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    //val noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
    private val noteViewModel: NoteViewModel by viewModels {
       NoteViewModelFactory((application.applicationContext as NoteApplication).repository)
    }
    var title: EditText? = null
    var note: EditText? = null
    var isEdit: Boolean? = false
    var noteItem: Note? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar!!.setTitle(resources.getString(R.string.note))
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        title = binding.title
        note = binding.note
        if (getIntent().getSerializableExtra("note") != null) {
            noteItem = getIntent().getSerializableExtra("note") as Note
            isEdit = true
            title!!.setText(noteItem!!.title)
            note!!.setText(noteItem!!.note)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                if (title?.text!!.isEmpty() || note?.text!!.isEmpty()) {
                    Toast.makeText(this@ActivityNote, R.string.Error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (!isEdit!!){
                        val date = Date()
                        val note = Note(note?.text.toString(), title!!.text.toString(),date.time)
                        noteViewModel.insert(note)
                    }else {
                        noteViewModel.update(noteItem?.id,title?.text.toString(),note!!.text.toString())
                    }

                    finish()
                }

                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }
}