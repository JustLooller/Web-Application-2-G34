package it.polito.wa2.g34.server.ticketing.dto

import it.polito.wa2.g34.server.ticketing.entity.Message
import java.time.LocalDateTime

data class MessageDTO(
    var id: Long,
    var text: String,
    var timestamp: LocalDateTime,
    var user_mail: String,
    var attachment: String?,
    var ticket_id: Long,
)

fun Message.toDTO() : MessageDTO{
    return MessageDTO(
        id= this.id,
        text=this.text,
        timestamp= this.timestamp,
        user_mail= this.user.email,
        attachment = this.attachment,
        ticket_id = this.ticket.id!!,
    )
}

//fun MessageDTO.toEntity(sale: SaleService) : Message {
//    return Message(
//        id=this.id,
//        text = this.text,
//        timestamp = this.timestamp,
//        user = this.user.toEntity(),
//        attachment = this.attachment,
//        ticket = this.ticket.toEntity(sale),
//    )
//}