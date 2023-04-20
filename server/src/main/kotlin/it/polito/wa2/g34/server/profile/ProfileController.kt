package it.polito.wa2.g34.server.profile

import jakarta.validation.constraints.Email
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class ProfileController(
    private val profileService: ProfileService,
) {
    @GetMapping("/api/profiles/{email}")
    fun getProfile(@PathVariable @Email email: String): ProfileDTO? {
        return profileService.getProfile(email) ?: throw ProfileNotFoundException("Profile not found")
    }

    @PostMapping("/api/profiles")
    fun postProfile(@RequestBody newProfile: ProfileDTO?): ProfileDTO? {
        if (newProfile != null) {
            return if (profileService.getProfile(newProfile.email) == null)
                profileService.postProfile(newProfile);
            else
                throw ProfileAlreadyExistException("Profile already exist")
        } else
            throw ProfileDataException("No body data")
    }

    @PutMapping("/api/profiles/{email}")
    fun putProfile(@RequestBody editedProfile: ProfileDTO?, @PathVariable @Email email: String): ProfileDTO? {
        if (editedProfile != null) {
            return if (profileService.getProfile(email) != null) {
                editedProfile.email = email;
                profileService.postProfile(editedProfile);
            } else throw ProfileNotFoundException("Profile not found")
        } else
            throw ProfileDataException("No body data")
    }

}


