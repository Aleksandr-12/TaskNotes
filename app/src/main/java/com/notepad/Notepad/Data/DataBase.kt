package com.notepad.Notepad.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.notepad.Notepad.Data.Dao.NoteDao
import com.notepad.Notepad.Data.Models.Note

@Database(entities = arrayOf(Note::class), version = 2, exportSchema = false)
abstract class DataBase  : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null

        fun getDatabase(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "NoteDataBase"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }

}