package it.polito.wa2.g34.server.ticketing.repository

import it.polito.wa2.g34.server.ticketing.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository: JpaRepository<Message, Long> {

    fun findAllByTicketId(ticketId: Long): List<Message>;
}