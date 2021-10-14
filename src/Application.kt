package com.notesapp

import com.notesapp.data.checkPasswordForEmail
import com.notesapp.data.collections.User
import com.notesapp.data.registerUser
import com.notesapp.routes.loginRoute
import com.notesapp.routes.noteRoutes
import com.notesapp.routes.registerRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders) //(optional) causes ktor to append all the extra info to all responses that come from our server
    install(CallLogging) //(optional) logs all the http requests that comes to the server and the responses
    install(ContentNegotiation){
        gson {
            setPrettyPrinting()
        }
    } //interprets content that is sent to/from the server (here json)
    install(Authentication) {
        configAuth()
    }
    install(Routing) {
        registerRoute()
        loginRoute()
        noteRoutes()
    } //makes sure we can define url endpoints where clients can connect to
}

private fun Authentication.Configuration.configAuth() {
    basic {
        realm = "Note Server" //Name of the server or something that would pop up in a browser
        validate { credentials ->
            val email = credentials.name
            val password = credentials.password
            if (checkPasswordForEmail(email, password)) {
                UserIdPrincipal(email) //Kind of a type of users to keep track of who is authenticated or not
            } else null
        }  //check name and password by defining a logic
    }
}