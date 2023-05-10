package it.polito.wa2.g34.server.ticketing.service.impl

import it.polito.wa2.g34.server.ticketing.converters.EntityConverter
import it.polito.wa2.g34.server.ticketing.dto.StateHistoryDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.UpdateTicketStatusDTO
import it.polito.wa2.g34.server.ticketing.entity.StateHistory
import it.polito.wa2.g34.server.ticketing.repository.StateHistoryRepository
import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class StateHistoryServiceImpl(
    private val stateHistoryRepository: StateHistoryRepository,
    private val entityConverter: EntityConverter
): StateHistoryService {

    fun StateHistoryDTO.toEntity() : StateHistory {
        return entityConverter.stateHistoryDTOtoEntity(this);
    }
    override fun getHistory(ticket: TicketDTO): List<StateHistory> {
        return stateHistoryRepository.findByTicketId(ticket.id!!);
    }

    override fun updateState(update: UpdateTicketStatusDTO) {
        val newState = StateHistoryDTO (
            id = null,
            ticket_id = update.ticket_id,
            timestamp = LocalDateTime.now(),
            status = update.newState,
            user_mail = update.requester_email,
        ).toEntity();
        // TODO(controlli di cambio di stato ammissibili).
        stateHistoryRepository.save(newState);
    }
}