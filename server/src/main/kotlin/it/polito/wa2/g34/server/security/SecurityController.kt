package it.polito.wa2.g34.server.security

import it.polito.wa2.g34.server.ticketing.service.TicketService
import jakarta.servlet.Filter
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException

data class UserLoginData(
    var username: String,
    var password: String
)

@RestController
@Validated
class SecurityController() {
    @Autowired
    lateinit var env: Environment

    @PostMapping("/api/login", produces = ["application/json"])
    fun postProfile(@Valid @RequestBody userLoginData: UserLoginData?): String? {

        val issuerUri = env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri")
        val clientId = env.getProperty("keykloak.client.id")
        val clientSecret = env.getProperty("keykloak.client.secret")

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val requestData = LinkedMultiValueMap<String, String>()
        requestData.add("username", userLoginData!!.username)
        requestData.add("password", userLoginData!!.password)
        requestData.add("client_id", clientId)
        requestData.add("client_secret", clientSecret)
        requestData.add("grant_type", "password")

        val requestEntity = HttpEntity(requestData, headers)
        try {
            val responseEntity = restTemplate.postForEntity(
                "$issuerUri/protocol/openid-connect/token",
                requestEntity,
                String::class.java
            )
            return responseEntity.body
        }
        catch (e: HttpClientErrorException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }
    }
}