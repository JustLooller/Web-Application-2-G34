package it.polito.wa2.g34.server.ticketing.service.impl

import it.polito.wa2.g34.server.ticketing.dto.MessageDTO
import it.polito.wa2.g34.server.ticketing.dto.toDTO
import it.polito.wa2.g34.server.ticketing.dto.toEntity
import it.polito.wa2.g34.server.ticketing.entity.Message
import it.polito.wa2.g34.server.ticketing.repository.MessageRepository
import it.polito.wa2.g34.server.ticketing.service.MessageService
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl(
    private val messageRepository : MessageRepository
): MessageService {

    override fun getChatMessages(ticketId: Long): List<Message> {
         return messageRepository.findAllByTicketId(ticketId);
    }

    override fun sendMessage(message: MessageDTO): Message {
        return messageRepository.save(message.toEntity());
    }
}