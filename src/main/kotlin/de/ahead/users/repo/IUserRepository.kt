package de.ahead.users.repo

import de.ahead.users.model.User

interface IUserRepository {

    fun findAll(): List<User>

    fun save(user: User): User

}