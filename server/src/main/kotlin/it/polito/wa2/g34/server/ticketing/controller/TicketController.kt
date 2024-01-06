package it.polito.wa2.g34.server.ticketing.controller

 import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g34.server.observability.LogInfo
 import it.polito.wa2.g34.server.profile.ProfileService
 import it.polito.wa2.g34.server.profile.Role
 import it.polito.wa2.g34.server.ticketing.dto.*
import it.polito.wa2.g34.server.ticketing.entity.Priority
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.service.MessageService
import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
import it.polito.wa2.g34.server.ticketing.service.TicketService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
 import org.springframework.security.core.context.SecurityContextHolder
 import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@CrossOrigin(origins = ["http://localhost:3000/", "http://localhost:5500/"])
@RestController
@Validated
@Observed
@LogInfo
class TicketController(
    private val ticketService: TicketService,
    private val stateHistoryService: StateHistoryService,
    private val messageService: MessageService,
    private val profileService: ProfileService,
) {

    private val logger = LoggerFactory.getLogger(TicketController::class.java);
    @GetMapping("/api/ticket/{id}")
    fun getTicket(@PathVariable("id") id: Long): TicketDTO {
        logger.debug("(MANUAL LOG) Getting ticket with id $id")
        return ticketService.getTicket(id).toDTO()
    }

    @GetMapping("/api/tickets")
    fun getUserTicket(): List<TicketDTO>? {
        val authentication = SecurityContextHolder.getContext().authentication
        val name = authentication.name
        val profile = profileService.getProfile(name)
        if (profile != null) {
            if (profile.role == Role.EXPERT) {
                return profile.let { profile1 -> ticketService.getTicketByExpert(profile1)?.map { it.toDTO() } ?: emptyList() }
            }
        }
        if (profile != null) {
            if (profile.role == Role.MANAGER) {
                return ticketService.getAllTickets().map { it.toDTO() }
            }
        }
        return ticketService.getTicketByEmail(profile!!).map { it.toDTO() }
    }

    @PostMapping("/api/ticket/")
    @PreAuthorize("#ticket.creator_email == authentication.name")
    fun createTicket(@Valid @RequestBody ticket: TicketDTO): TicketDTO {
        return ticketService.createTicket(ticket).toDTO()
    }
    @PutMapping("/api/ticket/{id}/start/{expert_id}") // ?priority=LOW (LOW = default)
    fun updateTicketState(
        @PathVariable("id") id: Long,
        @PathVariable("expert_id") @Email expertId: String,
        @RequestParam(name = "priority", defaultValue = "LOW") priority: Priority,
        principal: Principal
    ): TicketDTO {
        val requesterEmail = principal.name;
        val ticket = ticketService.getTicket(id)
        ticket.priority = priority
        return ticketService.assignExpert(ticket.toDTO(), expertId, requesterEmail).toDTO()
    }

    @PutMapping("/api/ticket/{id}/stop")
    fun stopTicket(@PathVariable("id") id: Long, principal: Principal) {
        val updateTicketStatusDTO = UpdateTicketStatusDTO(
            ticket_id = id,
            newState = State.OPEN.toString(),
            requester_email = principal.name
        )
        stateHistoryService.updateState(updateTicketStatusDTO);
    }

    @PutMapping("/api/ticket/{id}/close")
    fun closeTicket(@PathVariable("id") id: Long, principal: Principal) {
        val updateTicketStatusDTO = UpdateTicketStatusDTO(
            ticket_id = id,
            newState = State.CLOSED.toString(),
            requester_email = principal.name
        )
        stateHistoryService.updateState(updateTicketStatusDTO);
    }

    @PutMapping("/api/ticket/{id}/reopen")
    fun reopenTicket(@PathVariable("id") id: Long, principal: Principal) {
        val updateTicketStatusDTO = UpdateTicketStatusDTO(
            ticket_id = id,
            newState = State.REOPENED.toString(),
            requester_email = principal.name
        )
        stateHistoryService.updateState(updateTicketStatusDTO);
    }

    @PutMapping("/api/ticket/{id}/resolve")
    fun resolveTicket(@PathVariable("id") id: Long, principal: Principal) {
        val updateTicketStatusDTO = UpdateTicketStatusDTO(
            ticket_id = id,
            newState = State.RESOLVED.toString(),
            requester_email = principal.name
        )
        stateHistoryService.updateState(updateTicketStatusDTO);
    }

    @PostMapping("/api/ticket/{id}/message")
    @PreAuthorize("#messageDTO.user_mail == authentication.name")
    fun addMessageToTicket(@PathVariable("id") id: Long, @Valid @RequestBody messageDTO: MessageDTO) {
        messageDTO.ticket_id = id
        messageDTO.timestamp = LocalDateTime.now()
        messageService.sendMessage(messageDTO)
    }


    @GetMapping("/api/ticket/{id}/messages")
    fun getMessagesFromTicket(@PathVariable("id") id: Long): List<MessageDTO> {
        val messageList = messageService.getChatMessages(id).map { it.toDTO() }
        return messageList
    }

    @GetMapping("/api/ticket/{id}/history")
    fun getHistoryFromTicket(@PathVariable("id") id: Long): List<StateHistoryDTO> {
        val ticket = ticketService.getTicket(id)
        return stateHistoryService.getHistory(ticket.toDTO()).map { it.toDTO() }
    }
}