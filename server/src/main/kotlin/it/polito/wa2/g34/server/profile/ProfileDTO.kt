package it.polito.wa2.g34.server.profile

data class ProfileDTO(
    var email: String,
    var name: String,
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
