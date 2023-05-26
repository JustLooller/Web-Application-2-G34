package it.polito.wa2.g34.server.ticketing.controller

import it.polito.wa2.g34.server.ticketing.dto.*
import it.polito.wa2.g34.server.ticketing.entity.Priority
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.service.MessageService
import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
import it.polito.wa2.g34.server.ticketing.service.TicketService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@RestController
@Validated
class TicketController(
    private val ticketService: TicketService,
    private val stateHistoryService: StateHistoryService,
    private val messageService: MessageService,
) {

    @GetMapping("/api/ticket/{id}")
    fun getTicket(@PathVariable("id") id: Long): TicketDTO {
        return ticketService.getTicket(id).toDTO()
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
        return messageService.getChatMessages(id).map { it.toDTO() }
    }

    @GetMapping("/api/ticket/{id}/history")
    fun getHistoryFromTicket(@PathVariable("id") id: Long): List<StateHistoryDTO> {
        val ticket = ticketService.getTicket(id)
        return stateHistoryService.getHistory(ticket.toDTO()).map { it.toDTO() }
    }
}