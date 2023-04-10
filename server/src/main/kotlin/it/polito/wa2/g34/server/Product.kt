package it.polito.wa2.g34.server

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product")
class Product {
    @Id
    var ean: String=""
    var brand: Int=0
    var model: String = ""
    var description: String = ""
    var image: String = ""
}