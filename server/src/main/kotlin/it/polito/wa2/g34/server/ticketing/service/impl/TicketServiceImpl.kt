package it.polito.wa2.g34.server.ticketing.service.impl

import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g34.server.profile.*
import it.polito.wa2.g34.server.ticketing.advice.TicketBadRequestException
import it.polito.wa2.g34.server.ticketing.advice.TicketNotFoundException
import it.polito.wa2.g34.server.ticketing.converters.EntityConverter
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.UpdateTicketStatusDTO
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.entity.Ticket
import it.polito.wa2.g34.server.ticketing.repository.TicketRepository
import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
import it.polito.wa2.g34.server.ticketing.service.TicketService
import org.springframework.stereotype.Service

@Service
@Observed
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val profileRepository: ProfileRepository,
    private val stateHistoryService: StateHistoryService,
    private val entityConverter: EntityConverter,
) : TicketService {

    fun TicketDTO.toEntity() : Ticket {
        return entityConverter.ticketDTOtoEntity(this);
    }
    override fun getTicket(ticketId: Long): Ticket {
        return ticketRepository.findById(ticketId).orElseThrow { TicketNotFoundException("Ticket $ticketId not found") };
    }

    override fun createTicket(ticketDTO: TicketDTO): Ticket {
        if (ticketDTO.expert_mail != null) {
            throw TicketBadRequestException("Cannot create a ticket with an expert assigned")
        }
        if (ticketDTO.priority != null) {
            throw TicketBadRequestException("Cannot create a ticket with a priority assigned")
        }
        if (ticketDTO.state != State.OPEN.toString()) {
            throw TicketBadRequestException("Cannot create a ticket with a state assigned")
        }
        val ticketEntity = ticketDTO.toEntity()
        if (ticketEntity.creator.role != Role.CUSTOMER) {
            throw TicketBadRequestException("Only customers can create tickets")
        }
        if (ticketEntity.sale.product.ean != ticketDTO.product_ean) {
            throw TicketBadRequestException("The product ean must match the sale ean")
        }
        //TODO: Controllare esistenza di ticket associati allo stesso sale NON chiusi -> bloccare creazione

        return ticketRepository.save(ticketEntity);
    }

    override fun assignExpert(ticket: TicketDTO, expertMail: String, managerMail: String): Ticket {
        val expert = profileRepository.findById(expertMail);
        if (!expert.isPresent || expert.get().role != Role.EXPERT) {
            throw ProfileNotFoundException("No expert with id $expertMail exists");
        }
        val expertEntity = expert.get();
        val manager = profileRepository.findById(managerMail);
        if (!manager.isPresent || manager.get().role != Role.MANAGER) {
            throw ProfileNotFoundException("Manager with id $managerMail not found");
        }
        val managerDTO = manager.get();
        val update = UpdateTicketStatusDTO(
            ticket_id = ticket.id!!,
            requester_email = managerDTO.email,
            newState = State.IN_PROGRESS.toString(),
        )
        stateHistoryService.updateState(update);
        val ticket1 = ticketRepository.findById(ticket.id!!).get();
        ticket1.expert = expertEntity;
        ticket1.state = State.IN_PROGRESS
        return ticketRepository.save(ticket1);
    }

    override fun removeExpert(ticket: TicketDTO, requester: ProfileDTO): Ticket {
        // fetch profile from db to avoid request-tampering attacks
        val requesterId = profileRepository.findById(requester.email);
        if (!requesterId.isPresent) {
            throw ProfileNotFoundException("Requester with id ${requester.email} not found");
        }
        val requester1 = requesterId.get();
        if (requester1.email != ticket.expert_mail!!) {
            throw ProfileNotFoundException("The requester is not the assigned expert")
        } else if (requester1.role != Role.MANAGER) {
            throw ProfileNotFoundException("The requester is not a manager or the assigned expert")
        }
        ticket.expert_mail = null;
        return ticketRepository.save(ticket.toEntity());
    }

    override fun getTicketByEmail(profile: Profile): List<Ticket> {
        return ticketRepository.getByEmail(profile)
    }

}


