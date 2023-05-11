package it.polito.wa2.g34.server.ticketing.service.impl

import it.polito.wa2.g34.server.ticketing.advice.TicketBadRequestException
import it.polito.wa2.g34.server.ticketing.advice.TicketNotFoundException
import it.polito.wa2.g34.server.ticketing.converters.EntityConverter
import it.polito.wa2.g34.server.ticketing.dto.MessageDTO
import it.polito.wa2.g34.server.ticketing.entity.Message
import it.polito.wa2.g34.server.ticketing.repository.MessageRepository
import it.polito.wa2.g34.server.ticketing.service.MessageService
import it.polito.wa2.g34.server.ticketing.service.TicketService
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl(
    private val messageRepository: MessageRepository,
    private val ticketService: TicketService,
    private val entityConverter: EntityConverter,
) : MessageService {
    fun MessageDTO.toEntity(): Message {
        return entityConverter.messageDTOtoEntity(this);
    }

    override fun getChatMessages(ticketId: Long): List<Message> {
        ticketService.getTicket(ticketId); // assert ticket exists
        return messageRepository.findAllByTicketId(ticketId);
    }

    override fun sendMessage(message: MessageDTO): Message {
        // Check user is the owner or the expert of the ticket
        val ticket = ticketService.getTicket(message.ticket_id)
        if (ticket.creator.email != message.user_mail && ticket.expert?.email != message.user_mail) {
            throw TicketBadRequestException("User is not the owner or the expert of the ticket");
        }
        return messageRepository.save(message.toEntity());
    }
}