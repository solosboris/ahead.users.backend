package de.ahead.users.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.ArrayList
import java.util.UUID
import de.ahead.users.dto.UserDTO
import de.ahead.users.model.User
import de.ahead.users.mapper.UserMapper
import de.ahead.users.repo.IUserRepository

@Tag(name = "Users API", description = "Users management endpoints")
@RestController
@RequestMapping("/users")
class UsersController(
    private val userRepository: IUserRepository,
    private val userMapper: UserMapper
) {

    private val dash: String = "-"

    @Operation(
        summary = "Get all users",
        description = "Returns the complete list of all users",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of all users",
                content = [Content(schema = Schema(implementation = UserDTO::class))]
            )
        ]
    )
    @GetMapping
    fun getUsers(): List<UserDTO> =
        userMapper.modelToDTO(
            userRepository.findAll()
        )

    @Operation(
        summary = "Get an random users",
        description = "Returns an created user with arbitrary properties values",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "newly created random valued user",
                content = [Content(schema = Schema(implementation = UserDTO::class))]
            )
        ]
    )
    @GetMapping("/random")
    fun getRandomUser(): UserDTO {
        val uuid: String = UUID.randomUUID().toString()
        val randomName = uuid.substring(0, uuid.indexOf(dash))
        val randomSurname = uuid.substring(uuid.lastIndexOf(dash) + 1)

        val user: User = User(
            0,
            randomName,
            randomSurname,
            "$randomName.$randomSurname@example.com"
        )

        return userMapper.modelToDTO(
            userRepository.save(user)
        )
    }

}