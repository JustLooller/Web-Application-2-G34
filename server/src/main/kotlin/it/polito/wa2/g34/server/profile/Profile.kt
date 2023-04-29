package it.polito.wa2.g34.server.profile

import jakarta.persistence.*

@Entity
@Table(name = "profile")
class Profile(
    @Id
    var email: String = "",
    var name: String = "",
    var age: Int = 0,

    @Enumerated(EnumType.STRING)
    var role: Role = Role.CUSTOMER
)
