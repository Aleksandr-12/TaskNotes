package com.notepad.Notepad.Fragments.note

import androidx.lifecycle.*
import com.notepad.Notepad.Data.Models.Note
import com.notepad.Notepad.Data.Repository.NoteRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository:NoteRepository) : ViewModel() {

    val allNote: LiveData<List<Note>> = noteRepository.allNote.asLiveData()
    val allNoteFavorite: LiveData<List<Note>> = noteRepository.allNoteFavorite.asLiveData()

    fun insert(note: Note) = viewModelScope.launch {
        noteRepository.insert(note)
    }

    fun update(id: Int?, title: String?,note: String?) = viewModelScope.async {
        noteRepository.update(id, title,note)
    }

    fun deleteById(id: Int?) = viewModelScope.async {
        noteRepository.deleteById(id)
    }

    fun updateFavorite(id: Int?, favorite:Boolean) = viewModelScope.async {
        noteRepository.updateFavorite(id, favorite)
    }

}
class NoteViewModelFactory(val noteRepository:NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

