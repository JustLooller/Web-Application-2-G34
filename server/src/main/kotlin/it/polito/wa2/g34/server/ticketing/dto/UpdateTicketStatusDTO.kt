package it.polito.wa2.g34.server.ticketing.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import it.polito.wa2.g34.server.profile.ProfileDTO

data class UpdateTicketStatusDTO(
    var ticket: TicketDTO,
    var requester: ProfileDTO,
    @JsonIgnore
    var newState: String
)
