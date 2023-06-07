package it.polito.wa2.g34.server.security

import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g34.server.observability.LogInfo
import jakarta.validation.Valid
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException
import java.util.*

data class UserLoginData(
    var username: String,
    var password: String
)

@RestController
@Validated
@Observed
@LogInfo
class SecurityController() {
    @Autowired
    lateinit var keycloak: Keycloak
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

    @PostMapping("/api/signup", produces = ["application/json"])
    fun postSignUp(): String? {

        val userRepresentation = UserRepresentation()
        val credentialRepresentation = CredentialRepresentation()

        userRepresentation.email = "pippo@topolino.it"
        userRepresentation.username = "pippo@topolino.it"
        userRepresentation.isEnabled = true
        userRepresentation.realmRoles = listOf("CUSTOMER")

        credentialRepresentation.type = CredentialRepresentation.PASSWORD;
        credentialRepresentation.value = "password";
        credentialRepresentation.isTemporary = false;
        userRepresentation.credentials = listOf(credentialRepresentation);

        return keycloak.realm("WA2_G34")
            .users()
            .create(userRepresentation).toString();

        //return ""

        /*
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

         */
    }


}