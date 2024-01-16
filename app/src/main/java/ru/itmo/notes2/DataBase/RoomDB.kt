package ru.itmo.notes2.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.itmo.notes2.Models.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase() {

    companion object {
        private var database: RoomDB? = null
        private const val DATABASE_NAME = "NoteApp"

        fun getInstance(context: Context): RoomDB {
            return database ?: synchronized(this) {
                database ?: buildDatabase(context).also { database = it }
            }
        }

        private fun buildDatabase(context: Context): RoomDB {
            return Room.databaseBuilder(context, RoomDB::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun mainDao(): MainDao
}