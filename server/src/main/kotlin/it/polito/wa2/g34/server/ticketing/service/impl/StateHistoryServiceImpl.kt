package it.polito.wa2.g34.server.ticketing.service.impl

import it.polito.wa2.g34.server.ticketing.advice.IllegalUpdateException
import it.polito.wa2.g34.server.ticketing.advice.TicketNotFoundException
import it.polito.wa2.g34.server.ticketing.converters.EntityConverter
import it.polito.wa2.g34.server.ticketing.dto.StateHistoryDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.UpdateTicketStatusDTO
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.entity.StateHistory
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
            status = update.newState,
            user_mail = update.requester_email,
        ).toEntity();
        val ticket = ticketRepository.findById(update.ticket_id)
            .orElseThrow { throw TicketNotFoundException("Invalid ticket id provided"); }

        val validUpdateState = when (State.valueOf(update.newState)) {
            State.OPEN -> ticket.state == State.IN_PROGRESS;
            State.IN_PROGRESS -> ticket.state == State.OPEN || ticket.state == State.REOPENED;
            State.RESOLVED -> ticket.state == State.IN_PROGRESS || ticket.state == State.REOPENED || ticket.state == State.OPEN;
            State.REOPENED -> ticket.state == State.CLOSED || ticket.state == State.RESOLVED;
            State.CLOSED -> true; // valid from any state
        }
        if (!validUpdateState) {
            throw IllegalUpdateException("Invalid state update for ticket ${update.ticket_id} (from ${ticket.state} to ${update.newState})");
        }
        stateHistoryRepository.save(newState);
    }
}