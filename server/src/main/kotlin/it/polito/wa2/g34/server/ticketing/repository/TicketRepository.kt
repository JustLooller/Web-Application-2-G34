package it.polito.wa2.g34.server.ticketing.repository

import it.polito.wa2.g34.server.ticketing.entity.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository: JpaRepository<Ticket, Long> {
}