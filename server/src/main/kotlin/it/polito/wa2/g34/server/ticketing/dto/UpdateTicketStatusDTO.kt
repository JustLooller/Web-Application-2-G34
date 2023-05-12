package it.polito.wa2.g34.server.ticketing.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class UpdateTicketStatusDTO(
    @JsonIgnore
    var ticket_id: Long, // Automatically inferred from the path
    var requester_email: String,
    @JsonIgnore
    var newState: String? // Automatically inferred from the action
)
