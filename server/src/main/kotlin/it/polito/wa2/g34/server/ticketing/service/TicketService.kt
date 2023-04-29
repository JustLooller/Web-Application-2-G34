package it.polito.wa2.g34.server.ticketing.service

import it.polito.wa2.g34.server.profile.ProfileDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.entity.Ticket

interface TicketService {

    fun getTicket(ticketId: Long): Ticket?;

    fun createTicket(ticket: TicketDTO): Ticket;

    fun assignExpert(ticket: TicketDTO, expertId: String, managerId: String): Ticket;

    fun removeExpert(ticket: TicketDTO, requester: ProfileDTO): Ticket;


}