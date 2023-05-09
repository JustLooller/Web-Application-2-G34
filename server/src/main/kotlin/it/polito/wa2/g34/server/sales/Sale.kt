package it.polito.wa2.g34.server.sales

import it.polito.wa2.g34.server.product.Product
import it.polito.wa2.g34.server.profile.Profile
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Sale(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String,
    @ManyToOne
    var product: Product,
    @ManyToOne
    var buyer: Profile,

    var warranty_start: LocalDateTime = LocalDateTime.now(),
    var warranty_end: LocalDateTime,
)