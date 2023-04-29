package it.polito.wa2.g34.server.ticketing.service.impl

import it.polito.wa2.g34.server.profile.*
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.UpdateTicketStatusDTO
import it.polito.wa2.g34.server.ticketing.dto.toEntity
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.entity.Ticket
import it.polito.wa2.g34.server.ticketing.repository.TicketRepository
import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
import it.polito.wa2.g34.server.ticketing.service.TicketService
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val profileRepository: ProfileRepository,
    private val stateHistoryService: StateHistoryService,
) : TicketService {
    override fun getTicket(ticketId: Long): Ticket? {
        return ticketRepository.findById(ticketId).orElse(null);
    }

    override fun createTicket(ticket: TicketDTO): Ticket {
        return ticketRepository.save(ticket.toEntity());
    }

    override fun assignExpert(ticket: TicketDTO, expertId: String, managerId: String): Ticket {
        val expert = profileRepository.findById(expertId);
        if (!expert.isPresent || expert.get().role != Role.EXPERT) {
            throw ProfileNotFoundException("No expert with id $expertId exists");
        }
        val expertEntity = expert.get();
        val manager = profileRepository.findById(managerId);
        if (!manager.isPresent || manager.get().role != Role.MANAGER) {
            throw ProfileNotFoundException("Manager with id $managerId not found");
        }
        val managerDTO = manager.get();
        val update = UpdateTicketStatusDTO(
            ticket = ticket,
            requester = managerDTO.toDTO(),
            newState = State.IN_PROGRESS.toString(),
        )
        stateHistoryService.updateState(update);
        val ticket = ticketRepository.findById(ticket.id).get();
        ticket.expert = expertEntity;
        return ticketRepository.save(ticket);
    }

    override fun removeExpert(ticket: TicketDTO, requester: ProfileDTO): Ticket {
        // fetch profile from db to avoid request-tampering attacks
        val requesterId = profileRepository.findById(requester.email);
        if (!requesterId.isPresent) {
            throw ProfileNotFoundException("Requester with id ${requester.email} not found");
        }
        val requester = requesterId.get();
        if (requester.email != ticket.expert!!.email) {
            throw ProfileNotFoundException("The requester is not the assigned expert")
        } else if (requester.role != Role.MANAGER) {
            throw ProfileNotFoundException("The requester is not a manager or the assigned expert")
        }
        ticket.expert = null;
        return ticketRepository.save(ticket.toEntity());
    }

}


