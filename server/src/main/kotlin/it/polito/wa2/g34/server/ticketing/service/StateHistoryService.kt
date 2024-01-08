package it.polito.wa2.g34.server.ticketing.service

import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.UpdateTicketStatusDTO
import it.polito.wa2.g34.server.ticketing.entity.StateHistory

interface StateHistoryService {
    fun getHistory(ticket: TicketDTO) : List<StateHistory>;
    fun updateState(update: UpdateTicketStatusDTO);
    fun sortTicketsByMostRecentState(tickets: List<TicketDTO>): List<TicketDTO>
}