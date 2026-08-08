package com.example.notes_application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotesViewModel(

    private val repository: NoteRepository
) : ViewModel(){
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()
    init {
        viewModelScope.launch {

            repository.notes.collect { noteList ->

                _notes.value = noteList

            }

        }
    }
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
    fun addNote(note: Note){
        viewModelScope.launch {
            repository.addNote(note)
        }
    }
    fun updateNote(note : Note){
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }
}