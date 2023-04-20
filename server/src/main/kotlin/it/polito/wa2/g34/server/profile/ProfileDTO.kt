package it.polito.wa2.g34.server.profile

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class ProfileDTO(
    @Email
    var email: String,
    @Size(max = 100)
    var name: String,
    @Min(value = 0, message = "The value must be positive")
    @Max(value= 100, message = "The value must be under 100")
    var age: Int
)

fun Profile.toDTO(): ProfileDTO {
    return ProfileDTO(
        email = this.email,
        name = this.name,
        age = this.age
    )
}

fun ProfileDTO.toEntity(): Profile {
    return Profile(
        email = this.email,
        name = this.name,
        age = this.age
    )
}
