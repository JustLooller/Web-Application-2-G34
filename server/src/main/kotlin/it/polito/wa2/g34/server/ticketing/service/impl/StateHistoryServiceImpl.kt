package it.polito.wa2.g34.server.ticketing.service.impl

import it.polito.wa2.g34.server.profile.ProfileDTO
import it.polito.wa2.g34.server.ticketing.dto.StateHistoryDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.UpdateTicketStatusDTO
import it.polito.wa2.g34.server.ticketing.dto.toEntity
import it.polito.wa2.g34.server.ticketing.entity.StateHistory
import it.polito.wa2.g34.server.ticketing.repository.StateHistoryRepository
import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class StateHistoryServiceImpl(
    private val stateHistoryRepository: StateHistoryRepository,
): StateHistoryService {
    override fun getHistory(ticket: TicketDTO): List<StateHistory> {
        return stateHistoryRepository.findByTicketId(ticket.id);
    }

    override fun updateState(update: UpdateTicketStatusDTO) {
        val newState = StateHistoryDTO (
            id = 0,
            ticket = update.ticket,
            timestamp = LocalDateTime.now(),
            status = update.newState,
            user = update.requester,
        ).toEntity();
        // TODO: controlli di cambio di stato ammissibili.
        stateHistoryRepository.save(newState);
    }
}