package com.notepad.Notepad.Data.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Note")
class Note(var note:String,var title:String,var date:Long) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "favorite")
    var favorite: Boolean? = null

    @JvmName("getId1")
    fun getId(): Int {
        return id
    }

    @JvmName("setId1")
    fun setId(id: Int) {
        this.id = id
    }
    @JvmName("getFavorite1")
    fun getFavorite(): Boolean? {
        return favorite
    }

    @JvmName("setFavorite1")
    fun setFavorite(favorite: Boolean) {
        this.favorite = favorite
    }
}