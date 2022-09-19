package com.notepad.Notepad.Fragments.TasksServer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notepad.Notepad.Activity.ActivityAddTask
import com.notepad.Notepad.Activity.ActivityAuthorization
import com.notepad.Notepad.Activity.ActivityRegister
import com.notepad.Notepad.Adapters.TasksAdapter
import com.notepad.Notepad.DI.DaggerAppComponent
import com.notepad.Notepad.Data.Models.MarkBoolean
import com.notepad.Notepad.NoteApplication
import com.notepad.Notepad.Prefs.Prefs
import com.notepad.Notepad.R
import com.notepad.Notepad.RX.RxMark
import com.notepad.Notepad.Retrofit.Data.Success
import com.notepad.Notepad.Retrofit.Data.Tasks
import com.notepad.Notepad.Retrofit.RetrofitApi
import com.notepad.Notepad.Retrofit.TasksViewModel
import com.notepad.Notepad.Retrofit.TasksViewModelFactory
import com.notepad.Notepad.databinding.FragmentTasksBinding
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasksFragment : Fragment() {
    private val p = Paint()
    private var _binding: FragmentTasksBinding? = null
    private var alertDialog: AlertDialog.Builder? = null
    private val tasksViewModel: TasksViewModel by viewModels {
        TasksViewModelFactory()
    }
    lateinit var mService: RetrofitApi
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: TasksAdapter
    private val binding get() = _binding!!
    lateinit var recyclerList: RecyclerView
    var tasks: MutableList<Tasks>? = null
    val prefs: Prefs by lazy {
        NoteApplication.prefs!!
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setHasOptionsMenu(true)
        recyclerList = binding.recyclerMovieList
      //  mService = Common.retrofitApi
        mService =  DaggerAppComponent.create().retrofit()
        recyclerList.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerList.layoutManager = layoutManager
        tasksViewModel.tasksList.observe(this, { allTasks ->
            tasks = allTasks as MutableList<Tasks>
            adapter = TasksAdapter(activity!!.applicationContext!!, tasks!!)
            recyclerList.adapter = adapter
            adapter.notifyDataSetChanged()
        })
        tasksViewModel.getTasksList(prefs.User_id)
        RxMark.getInstance()?.listenBoolean()
            ?.subscribe(getInputObserverMark(this))
        initSwipe()
        return root
    }
    private fun getInputObserverMark(tasksFragment: TasksFragment): Observer<MarkBoolean> {
        return object : Observer<MarkBoolean> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
            override fun onNext(bool: MarkBoolean) {
               if (bool.mark==true){
                   tasksViewModel.getTasksList(prefs.User_id)
               }
            }
        }
    }
    @DelicateCoroutinesApi
    fun retrofitLaunch(){
        GlobalScope.launch(Dispatchers.Main) {
            val response = mService.getTasksList(prefs.User_id).await()
            adapter = TasksAdapter(activity!!.applicationContext!!, response)
            adapter.notifyDataSetChanged()
            recyclerList.adapter = adapter
        }
        mService.getTasksListCall(prefs.User_id).enqueue(object : Callback<MutableList<Tasks>> {
            override fun onFailure(call: Call<MutableList<Tasks>>, t: Throwable) {
                Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<MutableList<Tasks>>, response: Response<MutableList<Tasks>>) {
                adapter = TasksAdapter(activity!!.applicationContext!!, response.body() as MutableList<Tasks>)
                adapter.notifyDataSetChanged()
                recyclerList.adapter = adapter
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
        for (i in 0 until menu.size()) {
            val drawable = menu.getItem(i).icon
            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
            }
        }
    }
    private fun initSwipe() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            @SuppressLint("InflateParams")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                alertDialog = AlertDialog.Builder(activity)
                val view: View? = layoutInflater.inflate(R.layout.alert_dialog_delete_note, null)
                alertDialog!!.setView(view)
                alertDialog!!.setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                    mService.deleteTask(tasks?.get(position)?.id!!).enqueue(object : Callback<Success> {
                        override fun onFailure(call: Call<Success>, t: Throwable) {
                            Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
                        }
                        override fun onResponse(call: Call<Success>, response: Response<Success>) {
                            tasksViewModel.getTasksList(prefs.User_id)
                            Toast.makeText(context, R.string.deleteSuccess, Toast.LENGTH_LONG).show()
                        }
                    })
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                alertDialog!!.setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                alertDialog!!.setTitle(resources.getString(R.string.isDeleteNote))
                alertDialog!!.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val icon: Bitmap
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#D32F2F"))
                        val background = RectF(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(), dX, itemView.bottom.toFloat()
                        )
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete)
                        val icon_dest = RectF(
                            itemView.left.toFloat() + width,
                            itemView.top
                                .toFloat() + width,
                            itemView.left.toFloat() + 2 * width,
                            itemView.bottom
                                .toFloat() - width
                        )
                        c.drawBitmap(icon, null, icon_dest, p)
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"))
                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(), itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete)
                        val icon_dest = RectF(
                            itemView.right.toFloat() - 2 * width,
                            itemView.top
                                .toFloat() + width,
                            itemView.right.toFloat() - width,
                            itemView.bottom
                                .toFloat() - width
                        )
                        c.drawBitmap(icon, null, icon_dest, p)
                    }
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            val intent: Intent
            if(prefs.User_id.equals(-1)){
                intent = Intent(activity, ActivityAuthorization::class.java)
                startActivity(intent)
            }else{
                intent = Intent(activity, ActivityAddTask::class.java)
                startActivity(intent)
            }
            return true
        }else if (item.itemId == R.id.update) {
            if(prefs.User_id.equals(-1)){
                val intent = Intent(activity, ActivityAuthorization::class.java)
                startActivity(intent)
            }else{
                tasksViewModel.getTasksList(prefs.User_id)
            }
            return true
        }else if (item.itemId == R.id.register) {
            val intent: Intent = Intent(activity, ActivityRegister::class.java)
            startActivity(intent)
            return true
        }
        else if (item.itemId == R.id.exit) {
            prefs.User_id = -1
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}