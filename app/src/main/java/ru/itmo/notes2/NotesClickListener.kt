package ru.itmo.notes2

import androidx.cardview.widget.CardView
import ru.itmo.notes2.Models.Notes

interface NotesClickListener {
    fun onClick(notes: Notes)
    fun onLongClick(notes: Notes, cardView: CardView)
}