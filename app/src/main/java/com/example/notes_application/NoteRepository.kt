package com.example.notes_application

class NoteRepository(
    private val noteDao: NoteDao
) {

    val notes = noteDao.getAllNotes()

    suspend fun addNote(note: Note){
        noteDao.addNote(note)
    }

    suspend fun deleteNote(note: Note){
        noteDao.deleteNote(note)
    }

    suspend fun updateNote(note: Note){
        noteDao.updateNote(note)
    }
}