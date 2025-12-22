package de.ahead.users.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.options

@SpringBootTest
@AutoConfigureMockMvc
class TestCorsIntegration {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun testPreflightVueDevServerAllowedAll() {
        mockMvc.options("/ahead/users")
            .header("Origin", "http://localhost:5173")
            .header("Access-Control-Request-Method", "GET")
            .andExpect {
                status { isOk() }
                header {
                    exists("Access-Control-Allow-Origin")
                    exists("Access-Control-Allow-Methods")
                }
            }
    }

    @Test
    fun testPreflightVueDevServerAllowedRandom() {
        mockMvc.options("/ahead/users/random")
            .header("Origin", "http://localhost:5173")
            .header("Access-Control-Request-Method", "GET")
            .andExpect {
                status { isOk() }
                header {
                    exists("Access-Control-Allow-Origin")
                    exists("Access-Control-Allow-Methods")
                }
            }
    }

}