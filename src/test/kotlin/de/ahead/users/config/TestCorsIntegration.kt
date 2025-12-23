package de.ahead.users.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class TestCorsIntegration {

    @Autowired
    lateinit var mockMvc: MockMvc
    private var frontendRootURL: String = "http://localhost:5173"


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

    @Test
    fun testPreflightAllowedOrigins() {
        val allowedOrigins = listOf(
            frontendRootURL,
            "http://localhost:8080"
        )

        allowedOrigins.forEach { origin ->
            mockMvc.perform(
                options("/users")  // Test endpoint
                    .header(HttpHeaders.ORIGIN, origin)
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
            ).andExpect(status().isOk
            ).andExpect(
                header().string(
                    HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin
                )
            ).andExpect(
                header().string(
                    HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,OPTIONS"
                )
            )
        }
    }

    @Test
    fun testPreflightDisallowedOrigins() {
        mockMvc.perform(
            options("/users")
                .header(HttpHeaders.ORIGIN, "http://malicious.com")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
        ).andExpect(status().isForbidden)
    }

    @Test
    fun testPreflightAllowedOriginsCORSHeaders() {
        mockMvc.perform(
            get("/users")
                .header(HttpHeaders.ORIGIN, "http://localhost:5173")
        ).andExpect(status().isOk
        ).andExpect(
            header().string(
                    HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, frontendRootURL
                )
        )
    }

}