package de.ahead.users.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class TestCorsIntegration {

    @Autowired
    lateinit var mockMvc: MockMvc
    var frontendRootURL: String = "http://localhost:5173"

    @Test
    fun testPreflightVueDevServerAllowedAll() {
        mockMvc.perform(
            options("/ahead/users")
                .header("Origin", frontendRootURL)
                .header("Access-Control-Request-Method", "GET")
        ).andExpect(status().isOk
        ).andExpect(
            header().string(
                "Access-Control-Allow-Origin", frontendRootURL
            )
        )
    }

    @Test
    fun testPreflightVueDevServerAllowedRandom() {
        mockMvc.perform(
            options("/ahead/users/random")
                .header("Origin", frontendRootURL)
                .header("Access-Control-Request-Method", "GET")
        ).andExpect(status().isOk
        ).andExpect(
            header().string(
                "Access-Control-Allow-Origin", frontendRootURL
            )
        )
    }

    @Test
    fun testUnallowedEndpoint() {
        // no authentication → 403
        mockMvc.perform(get("/ahead/endpoint"))
            .andExpect(status().isForbidden)
    }

    @Test
    fun testSwaggerUIAccessible() {
        // Swagger is not under /ahead for matchers
        mockMvc.perform(get("/swagger-ui/index.html"))
            .andExpect(status().isOk)
    }

    @Test
    fun testSwaggerUiStaticResourcesAccessible() {
        mockMvc.perform(get("/swagger-ui/swagger-ui.css"))
            .andExpect(status().isOk)
    }

    @Test
    fun testOpenAPIAccessible() {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk)
            .andExpect(
                content()
                .contentTypeCompatibleWith("application/json")
            )
    }

}