package it.polito.wa2.g34.server.ticketing.service.impl

import it.polito.wa2.g34.server.ticketing.advice.IllegalUpdateException
import it.polito.wa2.g34.server.ticketing.advice.TicketNotFoundException
import it.polito.wa2.g34.server.ticketing.converters.EntityConverter
import it.polito.wa2.g34.server.ticketing.dto.StateHistoryDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.UpdateTicketStatusDTO
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.entity.StateHistory
import it.polito.wa2.g34.server.ticketing.entity.Ticket
import it.polito.wa2.g34.server.ticketing.repository.StateHistoryRepository
import it.polito.wa2.g34.server.ticketing.repository.TicketRepository
import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class StateHistoryServiceImpl(
    private val stateHistoryRepository: StateHistoryRepository,
    private val ticketRepository: TicketRepository,
    private val entityConverter: EntityConverter,
) : StateHistoryService {

    fun StateHistoryDTO.toEntity(): StateHistory {
        return entityConverter.stateHistoryDTOtoEntity(this);
    }

    override fun getHistory(ticket: TicketDTO): List<StateHistory> {
        return stateHistoryRepository.findByTicketId(ticket.id!!);
    }

    override fun updateState(update: UpdateTicketStatusDTO) {
        val newState = StateHistoryDTO(
            id = null,
            ticket_id = update.ticket_id,
            timestamp = LocalDateTime.now(),
            status = update.newState!!,
            user_mail = update.requester_email,
        ).toEntity();
        val ticket = ticketRepository.findById(update.ticket_id)
            .orElseThrow { throw TicketNotFoundException("Invalid ticket id provided"); }

        val validUpdateState = when (State.valueOf(update.newState!!)) {
            State.OPEN -> ticket.state == State.IN_PROGRESS;
            State.IN_PROGRESS -> ticket.state == State.OPEN || ticket.state == State.REOPENED;
            State.RESOLVED -> ticket.state == State.IN_PROGRESS || ticket.state == State.REOPENED || ticket.state == State.OPEN;
            State.REOPENED -> ticket.state == State.CLOSED || ticket.state == State.RESOLVED;
            State.CLOSED -> true; // valid from any state
        }
        if (!validUpdateState) {
            throw IllegalUpdateException("Invalid state update for ticket ${update.ticket_id} (from ${ticket.state} to ${update.newState})");
        }

        ticket.state = State.valueOf(update.newState!!);
        when (ticket.state) {
            State.OPEN -> {
                ticket.priority = null;
                ticket.expert = null;
            };
            State.IN_PROGRESS -> {};
            State.RESOLVED -> {};
            State.REOPENED -> {
                ticket.priority = null;
                ticket.expert = null;
            };
            State.CLOSED -> {
                ticket.priority = null;
                ticket.expert = null;
            };
        }

        ticketRepository.save(ticket);
        stateHistoryRepository.save(newState);
    }

    override fun sortTicketsByMostRecentState(tickets: List<TicketDTO>): List<TicketDTO> {
        val ticketsWithHistory: List<List<Any>> = tickets.map { ticket ->
            val history = getHistory(ticket);
            val mostRecentState = history.maxByOrNull { it.timestamp }?.timestamp ?: LocalDateTime.MIN;
            listOf(mostRecentState, ticket)
        }
        return ticketsWithHistory.sortedByDescending { it[0] as LocalDateTime }.map { it[1] as TicketDTO };
    }
}