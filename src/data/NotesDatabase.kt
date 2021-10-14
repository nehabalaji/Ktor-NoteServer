package com.notesapp.data

import com.notesapp.data.collections.Note
import com.notesapp.data.collections.User
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("NotesDatabase")
private val users = database.getCollection<User>()
private val notes = database.getCollection<Note>()

suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email: String): Boolean {
    return users.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String, passwordToBeChecked: String): Boolean {
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return actualPassword == passwordToBeChecked
}

suspend fun getNotesForUser(email: String) : List<Note> {
    return notes.find(Note::owners contains email).toList()
}

suspend fun saveNote(note: Note): Boolean {
    val notesExist = notes.findOneById(note.id) != null //Check if note already exists
    return if (notesExist) {
        notes.updateOneById(note.id, note).wasAcknowledged()
    } else {
        notes.insertOne(note).wasAcknowledged()
    }  //if note exists the update the note else insert the note
}