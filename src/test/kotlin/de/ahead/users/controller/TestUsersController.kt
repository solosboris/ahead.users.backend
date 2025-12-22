package de.ahead.users.controller

import de.ahead.users.dto.UserDTO
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@SpringBootTest
@AutoConfigureMockMvc
class TestUsersController {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val objectMapper = jacksonObjectMapper()
    private val log = KotlinLogging.logger {}

    @Test
    fun getAllUsers() {
        mockMvc.get("/ahead/users")
            .andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(3) }
            }
    }

    @Test
    fun getRandomUser() {
        val result = mockMvc.get("/ahead/users/random")
            .andExpect {
                status { isOk() }
            }
            .andReturn()

        val user = objectMapper.readValue(
            result.response.contentAsString,
            UserDTO::class.java
        )

        log.info { "Randomly generated user: $user" }

        assertEquals(4, user.id)

        mockMvc.get("/ahead/users")
            .andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(4) }
            }
    }

    @Test
    fun postRandomUser_shouldReturnMethodNotAllowed() {
        mockMvc.post("/ahead/users/random")
            .andExpect {
                status { isMethodNotAllowed() }
            }
    }

    @Test
    fun unknownEndpoint_shouldReturnNotFound() {
        mockMvc.get("/ahead/users/does-not-exist")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun wrongBasePath_shouldReturnNotFound() {
        mockMvc.get("/users")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun deleteUsers_shouldReturnMethodNotAllowed() {
        mockMvc.delete("/ahead/users")
            .andExpect {
                status { isMethodNotAllowed() }
            }
    }

    @Test
    fun openApiSpec_shouldBeAvailable() {
        //OpenAPI availability test
        mockMvc.get("/v3/api-docs")
            .andExpect {
                status { isOk() }
                content {
                    contentType("application/json")
                }
                jsonPath("$.openapi") { exists() }
                jsonPath("$.paths['/ahead/users']") { exists() }
                jsonPath("$.paths['/ahead/users/random']") { exists() }
            }
    }

    @Test
    fun swaggerUi_shouldBeAvailable() {
        //Swagger UI availability test
        mockMvc.get("/swagger-ui/index.html")
            .andExpect {
                status { isOk() }
            }
    }

}