package it.polito.wa2.g34.server.security

import it.polito.wa2.g34.server.profile.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true, securedEnabled = true)
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.PUT, "/api/ticket/*/start/*").hasRole(Role.MANAGER.name)
            .requestMatchers(HttpMethod.PUT, "/api/ticket/*/stop").hasRole(Role.EXPERT.name)
            .requestMatchers(HttpMethod.PUT, "/api/ticket/*/resolve").hasAnyRole(Role.EXPERT.name, Role.CUSTOMER.name)
            .requestMatchers(HttpMethod.POST, "/api/ticket/*/message").hasAnyRole(Role.EXPERT.name, Role.CUSTOMER.name)
            .requestMatchers(HttpMethod.POST, "/api/ticket/").hasRole(Role.CUSTOMER.name)
            .requestMatchers(HttpMethod.POST, "/api/ticket/*/reopen").hasRole(Role.CUSTOMER.name)
            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
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
        converter.setPrincipalClaimName("name")
        return converter

    }
}


