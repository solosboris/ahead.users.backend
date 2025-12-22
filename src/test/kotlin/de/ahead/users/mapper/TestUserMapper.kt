package de.ahead.users.mapper

import de.ahead.users.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestUserMapper {

    private val mapper: UserMapper = UserMapper()

    @Test
    fun testModel2DTO() {
        val user = User(
            id = 1,
            firstName = "Alice",
            lastName = "Arkansawyer",
            email = "alice.arkansawyer@example.com"
        )

        val dto = mapper.modelToDTO(user)

        assertEquals(user.id, dto.id)
        assertEquals(user.firstName, dto.firstName)
        assertEquals(user.lastName, dto.lastName)
        assertEquals(user.email, dto.email)
    }

}