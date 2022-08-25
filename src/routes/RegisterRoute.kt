package com.notesapp.routes

import com.notesapp.data.checkIfUserExists
import com.notesapp.data.collections.User
import com.notesapp.data.registerUser
import com.notesapp.data.requests.AccountRequest
import com.notesapp.data.responses.SimpleResponse
import com.notesapp.security.getHashWithSalt
import io.ktor.application.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerRoute() {
    route("/register") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            val userExists = checkIfUserExists(request.email)
            if (!userExists) {
                if(registerUser(User(request.email, getHashWithSalt(request.password)))) {
                    call.respond(OK, SimpleResponse(true, "Successfully created account."))
                }
                else {
                    call.respond(OK, SimpleResponse(false, "An unknown error occurred."))
                }
            }
            else {
                call.respond(OK, SimpleResponse(false, "A user with the email address already exists."))
            }
        }
    }
} //to specify the endpoints of the url