package com.example.notes_application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NotesViewModel : ViewModel(){
        val notes = mutableStateListOf<Note>(
            Note(
                title = "Shopping",
                description = "Buy Milk",
                date ="28 July"
            ),
            Note(
                title = "Study",
                description = "Complete Compose",
                date = "29 July"
            )
        )
    fun deleteNote(note: Note) {
        notes.remove(note)
    }
    fun addNote(note: Note){
        notes.add(note)
    }
    fun updateNote(oldNote: Note, newNote: Note){
        val index = notes.indexOf(oldNote)

        if(index != -1){
            notes[index] = newNote
        }
    }
}