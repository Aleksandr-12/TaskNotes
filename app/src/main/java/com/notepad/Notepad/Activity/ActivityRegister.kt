package com.notepad.Notepad.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.notepad.Notepad.DI.DaggerAppComponent
import com.notepad.Notepad.NoteApplication
import com.notepad.Notepad.Prefs.Prefs
import com.notepad.Notepad.R
import com.notepad.Notepad.Retrofit.Common.Common
import com.notepad.Notepad.Retrofit.Data.Register
import com.notepad.Notepad.Retrofit.Data.Tasks
import com.notepad.Notepad.Retrofit.RetrofitApi
import com.notepad.Notepad.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivityRegister  : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    var title: EditText? = null
    var note: EditText? = null
    lateinit var mService: RetrofitApi
    private var progressBar: ProgressBar? = null
    val prefs: Prefs by lazy {
        NoteApplication.prefs!!
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar!!.setTitle(resources.getString(R.string.task))
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val userName = binding.username
        val emailAddress = binding.email
        val password = binding.password
        val registerBtn: Button
        registerBtn = binding.register
       // mService = Common.retrofitApi
        mService =  DaggerAppComponent.create().retrofit()
        progressBar = binding.progressBar
        registerBtn.setOnClickListener { v: View? ->
            val user_name: String =
                Objects.requireNonNull(userName.getText()).toString()
            val email: String =
                Objects.requireNonNull(emailAddress.getText()).toString()
            val txt_password: String =
                Objects.requireNonNull(password.getText()).toString()
            if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(
                    txt_password
                )
            ) {
                Toast.makeText(this@ActivityRegister, "All fields are required", Toast.LENGTH_SHORT)
                    .show()
            } else {
                register(user_name, email, txt_password)
            }
        }
    }
    private fun register(name: String, email: String, password: String) {
        progressBar!!.visibility = View.VISIBLE
        mService.registerUser(name,password, email).enqueue(object :
            Callback<Register> {
            override fun onFailure(call: Call<Register>, t: Throwable) {
                Log.d("failer", (call.toString()) + " "+t.localizedMessage)
                Toast.makeText(this@ActivityRegister, R.string.error_register, Toast.LENGTH_SHORT)
                    .show()
            }
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                val g = (response.body() as Register)
                g.user_id
                Log.d("succes", ((response.body() as Register).toString()))
                 Log.d("succes2", g.user_id.toString())
                g.result?.let { Log.d("succes3", it) }
                prefs.User_id = g.user_id!!
                Log.d("succes2prefs?.USER_ID  /", prefs.User_id.toString())
                progressBar!!.visibility = View.INVISIBLE
                Toast.makeText(this@ActivityRegister, R.string.success_register, Toast.LENGTH_SHORT)
                .show()

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