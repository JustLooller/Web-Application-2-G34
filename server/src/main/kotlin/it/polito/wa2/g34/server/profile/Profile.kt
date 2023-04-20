package it.polito.wa2.g34.server.profile

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "profile")
class Profile(
    @Id
    var email: String = "",
    var name: String = "",
    var age: Int = 0,
)
