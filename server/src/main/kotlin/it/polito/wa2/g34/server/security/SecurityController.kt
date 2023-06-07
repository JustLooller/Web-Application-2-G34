package it.polito.wa2.g34.server.security

import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g34.server.observability.LogInfo
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.keycloak.admin.client.CreatedResponseUtil
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

data class UserSignUpData(
    @field:Email
    var username: String,
    var password: String,
    var fullName: String,
    var age: Int
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
    fun postSignUp(@Valid @RequestBody userSignUpData: UserSignUpData?): String? {

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

        // user creation
        val createResponse = keycloak.realm("WA2_G34")
            .users()
            .create(userRepresentation)

        // user id retrieving
        val userId = CreatedResponseUtil.getCreatedId(createResponse)

        // get realm roles
        val customerRealmRole = keycloak.realm("WA2_G34").roles().get("CUSTOMER").toRepresentation()

        keycloak.realm("WA2_G34")
            .users()
            .get(userId)
            .roles()
            .realmLevel()
            .add(listOf(customerRealmRole))

        return ""

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