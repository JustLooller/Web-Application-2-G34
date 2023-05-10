package it.polito.wa2.g34.server.ticketing.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class UpdateTicketStatusDTO(
    var ticket_id: Long,
    var requester_email: String,
    @JsonIgnore
    var newState: String
)
