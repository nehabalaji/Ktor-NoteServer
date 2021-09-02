package com.notesapp

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders) //(optional) causes ktor to append all the extra info to all responses that come from our server
    install(CallLogging) //(optional) logs all of the http requests that comes to the server and the responses
    install(Routing) //makes sure we can define url endpoints where clients can connect to
    install(ContentNegotiation){
        gson {
            setPrettyPrinting()
        }
    } //interprets content that is sent to/from the server (here json)
}

