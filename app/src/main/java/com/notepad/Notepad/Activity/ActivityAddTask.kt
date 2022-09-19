package com.notepad.Notepad.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.notepad.Notepad.DI.DaggerAppComponent
import com.notepad.Notepad.Data.Models.MarkBoolean
import com.notepad.Notepad.NoteApplication
import com.notepad.Notepad.Prefs.Prefs
import com.notepad.Notepad.R
import com.notepad.Notepad.RX.RxMark
import com.notepad.Notepad.Retrofit.Data.Success
import com.notepad.Notepad.Retrofit.Data.Tasks
import com.notepad.Notepad.Retrofit.RetrofitApi
import com.notepad.Notepad.databinding.ActivityNoteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivityAddTask : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding

    var title: EditText? = null
    var note: EditText? = null
    lateinit var mService: RetrofitApi
    val prefs: Prefs by lazy {
        NoteApplication.prefs!!
    }
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar!!.setTitle(resources.getString(R.string.task))
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        title = binding.title
        note = binding.note
       // mService = Common.retrofitApi
        mService =  DaggerAppComponent.create().retrofit()
        if(prefs.User_id.equals(-1)){
            val intent = Intent(this, ActivityAuthorization::class.java)
            startActivity(intent)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                if (title?.text!!.isEmpty() || note?.text!!.isEmpty()) {
                    Toast.makeText(this@ActivityAddTask, R.string.Error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    mService.addTask(prefs.User_id,title?.text!!.toString(), note?.text!!.toString()).enqueue(object : Callback<Success> {
                        override fun onFailure(call: Call<Success>, t: Throwable) {
                            Toast.makeText(this@ActivityAddTask, R.string.error, Toast.LENGTH_LONG).show()
                        }
                        override fun onResponse(call: Call<Success>, response: Response<Success>) {
                            Toast.makeText(this@ActivityAddTask, R.string.makeSuccess, Toast.LENGTH_LONG).show()
                            RxMark.getInstance()?.publishBoolean(MarkBoolean(true))
                        }
                    })
                   onBackPressed()
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