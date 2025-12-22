package de.ahead.users.mapper

import de.ahead.users.dto.UserDTO
import de.ahead.users.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun modelToDTO(
        user: User
    ): UserDTO = user.run {
        UserDTO(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email
        )
    }

    fun modelToDTO(
        users: List<User>
    ): List<UserDTO> =
        users.map { modelToDTO(it) }

}