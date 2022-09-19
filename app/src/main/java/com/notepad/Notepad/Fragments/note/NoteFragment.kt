package com.notepad.Notepad.Fragments.note

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notepad.Notepad.Activity.ActivityNote
import com.notepad.Notepad.Adapters.NoteAdapter
import com.notepad.Notepad.NoteApplication
import com.notepad.Notepad.R
import com.notepad.Notepad.databinding.FragmentHomeBinding
import java.util.*

class NoteFragment : Fragment() {
    private val p = Paint()
  //  private lateinit var noteViewModel: NoteViewModel
    private var alertDialog: AlertDialog.Builder? = null
    lateinit var adapter:NoteAdapter
    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((activity?.application as NoteApplication).repository)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        //noteViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(NoteViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerview
        adapter = NoteAdapter(activity!!.applicationContext,noteViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        noteViewModel.allNote.observe(this, { notes ->
            notes?.let { adapter.submitList(it) }
        })

        val fab = binding.fab
        fab.setOnClickListener {
            val intent = Intent(context, ActivityNote::class.java)
            startActivity(intent)
        }
        initSwipe()
        return root
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
                    noteViewModel.deleteById(
                        Objects.requireNonNull(
                            noteViewModel.allNote.value?.get(position)
                        )?.id
                    )
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
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}