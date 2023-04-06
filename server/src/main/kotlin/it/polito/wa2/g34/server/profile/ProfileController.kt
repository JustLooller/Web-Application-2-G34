package it.polito.wa2.g34.server.profile

import jakarta.validation.constraints.Email
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@Validated
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping("/profiles/{email}")
    fun getProfile(@PathVariable @Email email: String ): ProfileDTO? {
        return profileService.getProfile(email)?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/profiles")
    fun postProfile(@RequestBody newProfile: ProfileDTO?): ProfileDTO?{
        if (newProfile != null) {
            return if(profileService.getProfile(newProfile.email) == null)
                profileService.postProfile(newProfile);
            else
                throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }else
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)//PERSONALIZZARE
    }

    @PutMapping("/profiles/{email}")
    fun putProfile(@RequestBody editedProfile: ProfileDTO?, @PathVariable @Email email: String ): ProfileDTO?{
        if (editedProfile != null) {
            return if(profileService.getProfile(email) != null) {
                editedProfile.email = email;
                profileService.postProfile(editedProfile);
            }
            else throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }else
            throw ResponseStatusException(HttpStatus.BAD_REQUEST) //PERSONALIZZARE
    }

}


