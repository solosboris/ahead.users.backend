package de.ahead.users.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.ahead.users.dto.UserDTO
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class TestUsersController {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val objectMapper = jacksonObjectMapper()
    private val log = KotlinLogging.logger {}

    @Test
    fun testGetAllUsers() {
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(
                jsonPath("$.length()").value(3)
            )
    }

    @Test
    fun testGetRandomUser() {
        val mvcResult = mockMvc.perform(
                get("/users/random")
            ).andExpect(status().isOk)
            .andReturn()

        val user = objectMapper.readValue(
            mvcResult.response.contentAsString,
            UserDTO::class.java
        )

        log.info { "Randomly generated user: $user" }

        // first random user after initial 3
        assertEquals(4, user.id)

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(
                jsonPath("$.length()").value(4)
            )
    }

    @Test
    fun postRandomUser_shouldReturnNotFound() {
        // no POST mapping exists
        mockMvc.perform(post("/users/random"))
            .andExpect(
                status().is4xxClientError
            )
    }

    @Test
    fun deleteUsers_shouldReturnNotFound() {
        // no DELETE mapping exists
        mockMvc.perform(delete("/users"))
            .andExpect(
                status().is4xxClientError
            )
    }

}