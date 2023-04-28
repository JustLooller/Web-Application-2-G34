package it.polito.wa2.g34.server.ticketing.entity

import it.polito.wa2.g34.server.profile.Profile
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class StateHistory(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long,

        @ManyToOne
        var ticket: Ticket,

        @Enumerated(EnumType.STRING)
        var status: State,

        @ManyToOne
        var user: Profile,

        var timestamp: LocalDateTime = LocalDateTime.now(),
        )