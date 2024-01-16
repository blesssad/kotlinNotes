package ru.itmo.notes2.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.itmo.notes2.Models.Notes
import ru.itmo.notes2.NotesClickListener
import ru.itmo.notes2.R
import android.widget.TextView

class NotesListAdapter(
    private val context: Context,
    private val list: List<Notes>,
    private val listener: NotesClickListener
) : RecyclerView.Adapter<NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false)
        return NotesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = list[position]

        with(holder) {
            textView_title.text = note.title
            textView_title.isSelected = true

            // holder.textView_notes.text = note.notes

            textView_date.text = note.date
            textView_date.isSelected = true

            notes_conteiner.setCardBackgroundColor(
                itemView.resources.getColor(R.color.white, null)
            )

            notes_conteiner.setOnClickListener {
                listener.onClick(note)
            }

            notes_conteiner.setOnLongClickListener {
                listener.onLongClick(note, notes_conteiner)
                true
            }
        }
    }

    override fun getItemCount(): Int = list.size
}

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val notes_conteiner: CardView = itemView.findViewById(R.id.notes_conteiner)
    val textView_title: TextView = itemView.findViewById(R.id.textView_title)
    // val textView_notes: TextView = itemView.findViewById(R.id.textView_notes)
    val textView_date: TextView = itemView.findViewById(R.id.textView_date)
}