package de.ahead.users.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfigurationSource
import kotlin.test.assertNotNull

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    lateinit var context: ApplicationContext

    @Test
    fun testSecurityFilterChain() {
        val bean = context.getBean(
            SecurityFilterChain::class.java
        )
        assertNotNull(bean)
    }

    @Test
    fun testCORSConfigurationSource() {
        val bean = context.getBean(
            CorsConfigurationSource::class.java
        )
        assertNotNull(bean)
    }

}