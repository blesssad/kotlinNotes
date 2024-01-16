package ru.itmo.notes2.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import ru.itmo.notes2.Models.Notes

@Dao
interface MainDao {
    @Insert(onConflict = REPLACE)
    fun insert(notes: Notes)

    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY id DESC")
    fun getAll(): List<Notes>

    @Query("UPDATE notes SET title = :title, notes = :notes WHERE ID = :id")
    fun update(id: Int, title: String, notes: String)

    @Delete
    fun delete(notes: Notes)

    @Query("SELECT * FROM notes WHERE isDeleted = 1 ORDER BY id DESC")
    fun getDeletedNotes(): List<Notes>

    @Query("UPDATE notes SET isDeleted = 1 WHERE id = :id")
    fun deleteNote(id: Int)

    @Query("UPDATE notes SET isDeleted = 0 WHERE id = :id")
    fun restoreNote(id: Int)
}