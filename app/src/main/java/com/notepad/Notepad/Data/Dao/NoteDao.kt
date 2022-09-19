package com.notepad.Notepad.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.notepad.Notepad.Data.Models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY date ASC")
    fun getNote(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE favorite =:isFavorite ORDER BY date ASC")
    fun getNoteAllFavorite(isFavorite:Boolean): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("UPDATE Note SET title = :title,note = :note WHERE id =:mId")
    suspend fun update(mId: Int?, title: String?,note: String?)

    @Query("UPDATE Note SET favorite =:favorite WHERE id =:mId")
    suspend fun updateFavorite(mId: Int?, favorite: Boolean?)

    @Query("DELETE FROM Note")
    suspend fun deleteAll()

    @Query("DELETE FROM Note WHERE id=:id")
    fun deleteById(id:Int)
}