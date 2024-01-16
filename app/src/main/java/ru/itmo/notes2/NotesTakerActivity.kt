package ru.itmo.notes2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ru.itmo.notes2.Models.Notes
import java.text.SimpleDateFormat
import java.util.Date

class NotesTakerActivity : AppCompatActivity() {
    lateinit var editText_title: EditText
    lateinit var imageView_save: Button
    lateinit var editText_notes: EditText
    lateinit var notes: Notes
    var isOldNote: Boolean = false

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_taker)

        editText_title = findViewById(R.id.editText_title)
        imageView_save = findViewById(R.id.save_button)
        editText_notes = findViewById(R.id.editText_notes)

        try {
            notes = intent.getSerializableExtra("old_note") as Notes
            editText_title.setText(notes.title)
            editText_notes.setText(notes.notes)
            isOldNote = true
        } catch (e :Exception){
            e.printStackTrace()
        }

        imageView_save.setOnClickListener {
            val title = editText_title.text.toString()
            val description = editText_notes.text.toString()

//            if (description.isEmpty()) {
//                Toast.makeText(this@NotesTakerActivity, "Enter description", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
            val date = Date()

            if (!isOldNote) {
                notes = Notes()
            }

            notes.title = title
            notes.notes = description
            notes.date = formatter.format(date)

            val intent = Intent()
            intent.putExtra("note", notes)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
}