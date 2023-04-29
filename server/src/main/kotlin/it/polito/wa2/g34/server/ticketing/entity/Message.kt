package it.polito.wa2.g34.server.ticketing.entity
import it.polito.wa2.g34.server.profile.Profile
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class Message(

        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long,

        var text: String,

        var timestamp: LocalDateTime = LocalDateTime.now(),

        @ManyToOne
        var user: Profile,

        var attachment: String? = null,

        @ManyToOne
        var ticket: Ticket,

        )