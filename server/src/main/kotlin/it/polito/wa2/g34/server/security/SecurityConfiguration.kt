package it.polito.wa2.g34.server.security

import it.polito.wa2.g34.server.profile.Role
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true, securedEnabled = true)
class SecurityConfig {
    @Autowired
    lateinit var env: Environment
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        http.cors()
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.PUT, "/api/changePassword").hasAnyRole(Role.CUSTOMER.name, Role.EXPERT.name, Role.MANAGER.name)
            .requestMatchers(HttpMethod.PUT, "/api/ticket/*/start/*").hasRole(Role.MANAGER.name)
            .requestMatchers(HttpMethod.POST, "/api/createExpert").hasRole(Role.MANAGER.name)
            .requestMatchers(HttpMethod.PUT, "/api/ticket/*/stop").hasRole(Role.EXPERT.name)
            .requestMatchers(HttpMethod.PUT, "/api/ticket/*/resolve").hasAnyRole(Role.EXPERT.name, Role.CUSTOMER.name)
            .requestMatchers(HttpMethod.POST, "/api/ticket/*/message").hasAnyRole(Role.EXPERT.name, Role.CUSTOMER.name)
            .requestMatchers(HttpMethod.POST, "/api/ticket/").hasRole(Role.CUSTOMER.name)
            .requestMatchers(HttpMethod.POST, "/api/ticket/*/reopen").hasRole(Role.CUSTOMER.name)
            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/signup").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/ws**").permitAll() //TODO remove it
            .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
            .anyRequest().authenticated();
        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
        return http.build()
    }
    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter{ jwt: Jwt ->
            val resourceAccess = jwt.getClaimAsMap("realm_access")
            val roles = resourceAccess["roles"].let { it as List<String> }.map{"ROLE_${it}"}
            roles.map {GrantedAuthority { it } }
        }
        converter.setPrincipalClaimName("email")
        return converter

    }

    @Bean
    fun keycloak(): Keycloak {
        val serverUrl = env.getProperty("keykloak.client.url")
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm("master")
            .username("admin")
            .password("admin")
            .clientId("admin-cli")
            .build()
    }
}


