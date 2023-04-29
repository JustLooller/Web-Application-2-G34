package it.polito.wa2.g34.server.ticketing.dto

import it.polito.wa2.g34.server.profile.Profile
import it.polito.wa2.g34.server.profile.ProfileDTO
import it.polito.wa2.g34.server.profile.toDTO
import it.polito.wa2.g34.server.profile.toEntity
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.entity.StateHistory
import java.time.LocalDateTime

data class StateHistoryDTO(
    var id: Long,
    var timestamp: LocalDateTime,
    var status: String,
    var ticket: TicketDTO,
    var user: ProfileDTO,
)

fun StateHistory.toDTO() : StateHistoryDTO {
    return StateHistoryDTO(
        id = this.id,
        ticket = this.ticket.toDTO(),
        timestamp = this.timestamp,
        status = this.status.toString(),
        user = this.user.toDTO(),
    )
}

fun StateHistoryDTO.toEntity() : StateHistory {
    return StateHistory(
        id = this.id,
        ticket = this.ticket.toEntity(),
        timestamp = this.timestamp,
        status = State.valueOf(this.status),
        user = this.user.toEntity(),
    )
}
