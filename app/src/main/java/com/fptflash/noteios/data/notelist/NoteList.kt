package com.fptflash.noteios.data.notelist

import com.fptflash.noteios.data.Checkable
import java.util.*

class NoteList : LinkedList<Note>(), Checkable {
    /**
     * Creates a note with the given parameters and saves it to file.
     * @param title Displayed title of the note.
     * @param content Contents of the note.
     * @param color Color of the note.
     */
    fun addNote(title: String, content: String, color: NoteColors) {
        this.push(Note(name = title, content = content, color = color))
    }

    /**
     * Small helper function to add a note object, used for undoing deletions
     */
    fun addFullNote(note: Note): Int {
        addNote(title = note.title, content = note.content ?: "", color = note.color)
        return this.indexOf(note)
    }

    override fun check() {
        this.forEach {
            if (it.color == null || it.title == null) {
                throw NullPointerException()
            }
        }
    }

}
