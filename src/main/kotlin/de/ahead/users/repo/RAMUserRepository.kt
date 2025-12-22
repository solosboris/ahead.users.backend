package de.ahead.users.repo

import de.ahead.users.model.User
import org.springframework.stereotype.Repository
import java.util.concurrent.CopyOnWriteArrayList

@Repository
class RAMUserRepository : IUserRepository {

    private val users: MutableList<User> = mutableListOf(
        User(1, "Alice", "Arkansawyer",
            "alice.arkansawyer@example.com"
        ),
        User(2, "Bob", "Bristolian",
            "bob.bristolian@example.com"
        ),
        User(3, "Charlie", "Connecticuter",
            "charlie.connecticuter@example.com"
        )
    )

    override fun findAll(): List<User> =
        users.toList()

    override fun save(user: User): User {
        // if any users had been removed
        // the max id value is still at the last user item
        val idToStore: Int = if (users.isEmpty()) 1 else users.last().id + 1
        val firstName = user.firstName
        val lastName = user.lastName
        val userToStore: User = User(
            idToStore,
            firstName,
            lastName,
            "$firstName.$lastName@example.com"
        )

        users.add(userToStore)
        return userToStore
    }

}