package it.polito.wa2.g34.server.ticketing.dto

import it.polito.wa2.g34.server.ticketing.entity.StateHistory
import java.time.LocalDateTime

data class StateHistoryDTO(
    var id: Long?,
    var timestamp: LocalDateTime,
    var status: String,
    var ticket_id: Long,
    var user_mail: String,
)

fun StateHistory.toDTO() : StateHistoryDTO {
    return StateHistoryDTO(
        id = this.id,
        ticket_id = this.ticket.id!!,
        timestamp = this.timestamp,
        status = this.status.toString(),
        user_mail = this.user.email,
    )
}
