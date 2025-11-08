package com.example

import com.example.repository.UserRepository
import io.ktor.htmx.html.hx
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.random.Random
import kotlinx.html.*
import io.ktor.server.htmx.hx
import io.ktor.server.request.receiveParameters
import kotlinx.html.stream.createHTML


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
                            target = "#table-body"
                            swap = "afterbegin"
                            on(":after-request", "this.reset()")
                        }
                        label { 
                            htmlFor = "first-name"
                            +"First Name: "
                         }
                        input {
                            name = "firstName"
                            id = "first-name"
                            placeholder="Abebe"
                        }
                        label { 
                            htmlFor = "last-name"
                            +"Last Name: "
                         }
                        input {
                            name = "lastName"
                            id = "last-name"
                            placeholder="Kebede"
                        }
                        
                        input {
                            type = InputType.radio
                            name = "enabled"
                            id = "enabled"
                            value = "true"
                        }
                        label { 
                            htmlFor = "enabled"
                            +"Enabled"
                         }
                        input {
                            type = InputType.radio
                            name = "enabled"
                            id = "disabled"
                            value = "false"
                        }
                        label { 
                            htmlFor = "disabled"
                            +"Disabled"
                         }
                        button { 
                            type = ButtonType.submit
                            +"Add User"
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
                            id = "table-body"
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
                                        delete = "/users/${user.id}"
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

        post("/users"){
            val formData = call.receiveParameters()
            val firstName = formData["firstName"]
            val lastName = formData["lastName"]
            val enabled = formData["enabled"]
            if(firstName == null || lastName == null || enabled == null){
                call.respond(status = HttpStatusCode.BadRequest, message = "all form fields are required")
            }
            else {
                val user = userRepository.create(firstName = firstName, lastName = lastName, enabled = if(enabled == "enabled") true else false)
                val newRow = createHTML().tr {
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
                                        delete = "/users/${user.id}"
                                        target = "closest tr"
                                        swap= "outerHTML"
                                       }
                                     +"Delete"
                                    }
                                }
                }
                call.respondText(newRow, contentType = ContentType.Text.Html)
            }
        }


        delete("/users/{id}"){
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
