package com.notepad.Notepad.Activity

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.notepad.Notepad.DI.DaggerAppComponent
import com.notepad.Notepad.Fragments.TasksServer.TasksFragment
import com.notepad.Notepad.NoteApplication
import com.notepad.Notepad.Prefs.Prefs
import com.notepad.Notepad.R
import com.notepad.Notepad.Retrofit.Common.Common
import com.notepad.Notepad.Retrofit.Data.Register
import com.notepad.Notepad.Retrofit.RetrofitApi
import com.notepad.Notepad.databinding.ActivityAuthorizationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivityAuthorization  : AppCompatActivity()  {
    private lateinit var binding: ActivityAuthorizationBinding

    var title: EditText? = null
    var note: EditText? = null
    lateinit var mService: RetrofitApi
    private var progressBar: ProgressBar? = null
    val prefs: Prefs by lazy {
        NoteApplication.prefs!!
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar!!.setTitle(resources.getString(R.string.task))
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val userName = binding.username
        val password = binding.password
        val registerBtn: Button
        registerBtn = binding.register
        //mService = Common.retrofitApi
        mService =  DaggerAppComponent.create().retrofit()
        progressBar = binding.progressBar
        registerBtn.setOnClickListener { v: View? ->
            val user_name: String =
                Objects.requireNonNull(userName.getText()).toString()
            val txt_password: String =
                Objects.requireNonNull(password.getText()).toString()
            if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(
                    txt_password
                )
            ) {
                Toast.makeText(this@ActivityAuthorization, "All fields are required", Toast.LENGTH_SHORT)
                    .show()
            } else {
                authorization(user_name, txt_password)
            }
        }
    }
    private fun authorization(name: String, password: String) {
        progressBar!!.visibility = View.VISIBLE
        mService.authorizationUser(name,password).enqueue(object :
            Callback<Register> {
            override fun onFailure(call: Call<Register>, t: Throwable) {
                Toast.makeText(this@ActivityAuthorization, R.string.error_authorization, Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                val g = (response.body() as Register)
                prefs.User_id = g.user_id!!
                progressBar!!.visibility = View.INVISIBLE
                Toast.makeText(this@ActivityAuthorization, R.string.success_authorization, Toast.LENGTH_SHORT)
                    .show()
                onBackPressed()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}