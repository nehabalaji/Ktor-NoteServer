package com.notesapp.routes

import com.notesapp.data.collections.Note
import com.notesapp.data.deleteNoteForUser
import com.notesapp.data.getNotesForUser
import com.notesapp.data.requests.DeleteNoteRequest
import com.notesapp.data.saveNote
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import java.lang.Exception
import kotlin.math.log

fun Route.noteRoutes() {
    route("/getNotes") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val notes = try {
                    getNotesForUser(email)
                } catch (e: Exception) {
                    call.respond(e.message.toString())
                    return@get
                }
                call.respond(OK, notes)
            }
        } //user must be authenticated in order to access the note
    }
    route("/addNote") {
        authenticate {
            post {
                val note = try {
                    call.receive<Note>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (saveNote(note)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
    route("/deleteNote") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteNoteRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (deleteNoteForUser(email, request.id)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict )
                }
            }
        }
    }
}