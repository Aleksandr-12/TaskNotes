package com.notepad.Notepad.Fragments.favoriteNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.notepad.Notepad.NoteApplication
import com.notepad.Notepad.databinding.FragmentFavoriteNoteBinding
import com.notepad.Notepad.Adapters.NoteAdapter
import com.notepad.Notepad.Fragments.note.NoteViewModel
import com.notepad.Notepad.Fragments.note.NoteViewModelFactory

class favoriteNoteFragment : Fragment() {

    private var _binding: FragmentFavoriteNoteBinding? = null
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((activity?.application as NoteApplication).repository)
    }
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteNoteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerview
        val adapter = NoteAdapter(activity!!.applicationContext, noteViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        noteViewModel.allNoteFavorite.observe(this, { notes ->
            notes?.let { adapter.submitList(it) }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}