package ru.itmo.notes2.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "notes")
class Notes() : Serializable{

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "notes")
    var notes: String = ""

    @ColumnInfo(name = "date")
    var date: String = ""

    @ColumnInfo(name = "isDeleted")
    var isDeleted: Boolean = false
}