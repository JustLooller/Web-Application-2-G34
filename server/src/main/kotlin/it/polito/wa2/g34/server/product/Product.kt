package it.polito.wa2.g34.server.product

import jakarta.persistence.*

@Entity
@Table(name = "product")
class Product (
    @Id
    var ean: String="",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand")
    var brand: Brand,
    var model: String = "",
    var description: String = "",
    var image: String = ""
)

@Entity
class Brand(
    @Id
    var id: Int=0,
    var name: String
)