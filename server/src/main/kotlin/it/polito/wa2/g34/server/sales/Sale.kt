package it.polito.wa2.g34.server.sales
import it.polito.wa2.g34.server.product.Product
import it.polito.wa2.g34.server.profile.Profile
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime
import java.util.UUID

@Entity
class Sale (
        @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    var id: UUID,
        @ManyToOne
    var product: Product,
        @ManyToOne
    var buyer: Profile,

        var warranty_start: LocalDateTime = LocalDateTime.now(),
        var warranty_end: LocalDateTime,
)