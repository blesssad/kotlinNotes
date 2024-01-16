package ru.itmo.notes2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.itmo.notes2.Adapter.NotesListAdapter
import ru.itmo.notes2.DataBase.RoomDB
import ru.itmo.notes2.Models.Notes


class MainActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var add: Button
    private lateinit var garbage: Button
    private lateinit var back: Button
    private lateinit var notesListAdapter: NotesListAdapter
    private lateinit var database: RoomDB
    private var notes: MutableList<Notes> = ArrayList()
    private lateinit var selectedNote: Notes

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()

        database = RoomDB.getInstance(this)
        notes = database.mainDao().getAll().toMutableList()
        updateRecyclerNotes(notes);


        add.setOnClickListener {
            val intent = Intent(this@MainActivity, NotesTakerActivity::class.java)
            startActivityForResult(intent, 101)
            garbage.visibility = View.VISIBLE
            back.visibility = View.INVISIBLE
        }

        garbage.setOnClickListener {
            val deletedNotes = database.mainDao().getDeletedNotes()
            notes.clear()
            notes.addAll(deletedNotes)
            notesListAdapter.notifyDataSetChanged()
            back.visibility = View.VISIBLE
            garbage.visibility = View.INVISIBLE
        }

        back.setOnClickListener{
            val allNotes = database.mainDao().getAll()
            notes.clear()
            notes.addAll(allNotes)
            notesListAdapter.notifyDataSetChanged()
            back.visibility = View.INVISIBLE
            garbage.visibility = View.VISIBLE
        }
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recycler_home)
        add = findViewById(R.id.add)
        garbage = findViewById(R.id.garbage)
        back = findViewById(R.id.back)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            101, 102 -> handleResult(requestCode, resultCode, data)
        }
    }

    private fun handleResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val newNotes = data?.getSerializableExtra("note") as Notes
            when (requestCode) {
                101 -> database.mainDao().insert(newNotes)
                102 -> database.mainDao().update(newNotes.id, newNotes.title, newNotes.notes)
            }
            updateNotesList(database.mainDao().getAll())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateNotesList(updatedNotes: List<Notes>) {
        notes.clear()
        notes.addAll(updatedNotes)
        notesListAdapter.notifyDataSetChanged()
    }

    private fun updateRecyclerNotes(notes: List<Notes>) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        notesListAdapter = NotesListAdapter(this@MainActivity, notes, notesClickListener)
        recyclerView.adapter = notesListAdapter
    }

    private val notesClickListener = object : NotesClickListener {
        override fun onClick(notes: Notes){
            val intent = Intent(this@MainActivity, NotesTakerActivity::class.java)
            intent.putExtra("old_note", notes)
            startActivityForResult(intent, 102)
        }

        override fun onLongClick(notes: Notes, cardView: CardView){
            selectedNote = Notes()
            selectedNote = notes
            showDeleteConfirmationDialog(selectedNote)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDeleteConfirmationDialog(note: Notes) {
        val noteToDelete = note.title

        val builder = AlertDialog.Builder(this)
        if (note.isDeleted != true){
            builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete note \"$noteToDelete\"?")
                .setPositiveButton("Delete") { _, _ ->
                    database.mainDao().deleteNote(note.id)
                    notes.remove(note)
                    notesListAdapter.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Note removed", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
        else{
            builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete note \"$noteToDelete\" forever ?")
                .setPositiveButton("Delete forever") { _, _ ->
                    database.mainDao().delete(selectedNote)
                    notes.remove(selectedNote)
                    notesListAdapter.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Note removed forever", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Restore"){ _, _, ->
                    database.mainDao().restoreNote(selectedNote.id)
                    notes.remove(selectedNote)
                    notesListAdapter.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Note restored", Toast.LENGTH_SHORT).show()
                }
                .create()
                .show()
        }
    }
}