package com.notesapp.routes

import com.notesapp.data.checkPasswordForEmail
import com.notesapp.data.requests.AccountRequest
import com.notesapp.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.loginRoute() {
    route("/login") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val isPasswordCorrect = checkPasswordForEmail(request.email, request.password)
            if (isPasswordCorrect) {
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "You are now logged in."))
            }
            else {
                call.respond(HttpStatusCode.OK, SimpleResponse(false, "The email is password is not correct."))
            }
        }
    }
}