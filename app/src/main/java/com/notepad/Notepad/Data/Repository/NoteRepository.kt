package com.notepad.Notepad.Data.Repository
import androidx.annotation.WorkerThread
import com.notepad.Notepad.Data.Dao.NoteDao
import com.notepad.Notepad.Data.Models.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository( private var noteDao: NoteDao) {

    val allNote: Flow<List<Note>> = noteDao.getNote()
    val allNoteFavorite: Flow<List<Note>> = noteDao.getNoteAllFavorite(true)

    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }
    @WorkerThread
    suspend fun update(id: Int?, title: String?,note: String?) {
        noteDao.update(id, title,note)
    }

    @WorkerThread
    suspend fun updateFavorite(id: Int?, favorite:Boolean) {
        noteDao.updateFavorite(id, favorite)
    }
    @WorkerThread
    fun deleteById(id: Int?) {
        if (id != null) {
            noteDao.deleteById(id)
        }
    }
}