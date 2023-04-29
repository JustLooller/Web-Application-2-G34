package it.polito.wa2.g34.server.ticketing.service

import it.polito.wa2.g34.server.ticketing.dto.MessageDTO
import it.polito.wa2.g34.server.ticketing.entity.Message

interface MessageService {
    fun getChatMessages(ticketId: Long): List<Message>;
    fun sendMessage(message: MessageDTO): Message;
}
