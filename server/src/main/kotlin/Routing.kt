package com.example

import com.example.repository.UserRepository
import io.ktor.htmx.html.hx
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.random.Random
import kotlinx.html.*
import io.ktor.server.htmx.hx


@io.ktor.utils.io.ExperimentalKtorApi
fun Application.configureRouting(userRepository:UserRepository) {
    routing {
        staticResources("/", "/web")


        get("/"){
            call.respondHtml {
                head { 
                    title("Users Page")
                    script(src="/web.js"){}
                 }
                body {
                    h1 { 
                        +"Users List"
                     }
                    form { 
                        attributes.hx {
                            post = "/users"
                            target = "closest tbody"
                            swap = "afterbegin"
                        }
                        label { 
                            htmlFor = "first-name"
                            +"First Name"
                         }
                        input {
                            name = "firstName"
                            id = "firstName"
                            placeholder="Abebe Kebede"
                        }
                     }
                    table { 
                        thead { 
                            tr { 
                                th { 
                                    +"ID"
                                 }
                                th { 
                                    +"First Name"
                                 }
                                th { 
                                    +"Last Name"
                                 }
                                th { 
                                    +"Enabled"
                                 }
                                th {
                                    +"Action"
                                }
                                
                             }
                         }
                        tbody { 
                            userRepository.findAll().forEach {user ->
                             tr { 
                                td { +user.id }
                                td { +user.firstName }
                                td { +user.lastName }
                                td { 
                                    val enabled = if(user.enabled) "Enabled" else "Disabled"
                                    +enabled
                                 }
                                td {
                                    button {  
                                      attributes.hx { 
                                        delete = "/user/${user.id}"
                                        target = "closest tr"
                                        swap= "outerHTML"
                                       }
                                     +"Delete"
                                    }
                                }
                              }
                            }
                         }
                     }
                  } 
              }
        }


        delete("/user/{id}"){
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(status = HttpStatusCode.BadRequest, message = "id not provided")
                }
            else {
                val response = userRepository.delete(id)
                println(response)
                call.respond(HttpStatusCode.OK)
                }
        }
    }
}
