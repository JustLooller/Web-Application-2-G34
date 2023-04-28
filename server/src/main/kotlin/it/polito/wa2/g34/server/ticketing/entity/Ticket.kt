package it.polito.wa2.g34.server.ticketing.entity

import it.polito.wa2.g34.server.product.Product
import it.polito.wa2.g34.server.profile.Profile
import it.polito.wa2.g34.server.sales.Sale
import jakarta.persistence.*

@Entity
class Ticket (
        @Id
        @GeneratedValue (strategy = GenerationType.SEQUENCE)
        var id : Long,
        var priority: Int,

        @Enumerated(EnumType.STRING)
        var state: State,

        @ManyToOne
        var creator: Profile,

        @ManyToOne
        var expert: Profile,

        @ManyToOne
        var product: Product,

        @ManyToOne
        var sale: Sale,

        @OneToMany(mappedBy = "ticket")
        var history: List<StateHistory>,

        @OneToMany(mappedBy = "ticket")
        var messages: List<Message>,
)