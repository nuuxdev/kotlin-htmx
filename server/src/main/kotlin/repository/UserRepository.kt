package com.example.repository

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val enabled:Boolean,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

class UserRepository {
    private val users: MutableList<User> = mutableListOf(
        User(firstName = "Abebe", lastName = "kebede", enabled = false),
        User(firstName = "Yared", lastName = "Shumetie", enabled = false),
        User(firstName = "Allebachew", lastName = "Teka", enabled = false),
        User(firstName = "Bedru", lastName = "Ahmed", enabled = true),
        User(firstName = "Lulit", lastName = "Solomon", enabled = true),
    )
    

    fun findAll():List<User> = users
    
    fun create(firstName:String, lastName:String, enabled:Boolean):User = User(firstName = firstName, lastName = lastName, enabled = enabled).also(users::add)

    fun delete(id:String):Boolean = users.removeIf {it.id == id}
}