package it.polito.wa2.g34.server.ticketing.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import it.polito.wa2.g34.server.profile.ProfileDTO
import it.polito.wa2.g34.server.profile.toDTO
import it.polito.wa2.g34.server.profile.toEntity
import it.polito.wa2.g34.server.ticketing.entity.Message
import java.time.LocalDateTime

data class MessageDTO(
    var id: Long,
    var text: String,
    var timestamp: LocalDateTime,
    var user: ProfileDTO,
    var attachment: String?,
    var ticket: TicketDTO,
)

fun Message.toDTO() : MessageDTO{
    return MessageDTO(
        id= this.id,
        text=this.text,
        timestamp= this.timestamp,
        user= this.user.toDTO(),
        attachment = this.attachment,
        ticket = this.ticket.toDTO(),
    )
}

fun MessageDTO.toEntity() : Message {
    return Message(
        id=this.id,
        text = this.text,
        timestamp = this.timestamp,
        user = this.user.toEntity(),
        attachment = this.attachment,
        ticket = this.ticket.toEntity(),
    )
}