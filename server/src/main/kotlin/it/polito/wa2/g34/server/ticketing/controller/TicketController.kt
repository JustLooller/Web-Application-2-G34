package it.polito.wa2.g34.server.ticketing.controller

import it.polito.wa2.g34.server.ticketing.advice.TicketNotFoundException
import it.polito.wa2.g34.server.ticketing.dto.*
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.service.MessageService
import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
import it.polito.wa2.g34.server.ticketing.service.TicketService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class TicketController(
    private val ticketService: TicketService,
    private val stateHistoryService: StateHistoryService,
    private val messageService: MessageService,
) {
    @GetMapping("/api/ticket/{id}")
    fun getTicket(@PathVariable("id") id: Long): TicketDTO {
        return ticketService.getTicket(id)?.toDTO() ?: throw TicketNotFoundException("Ticket with id: $id not Found")
    }

    @PostMapping("/api/ticket/")
    fun createTicket(@RequestBody ticket: TicketDTO): TicketDTO {
        return ticketService.createTicket(ticket).toDTO()
    }

    @PutMapping("/api/ticket/{id}/start/{expert_id}")
    fun updateTicketState(
        @PathVariable("id") id: Long,
        @PathVariable("expert_id") @Email expertId: String,
        @Valid @RequestBody updateTicketStatusDTO: UpdateTicketStatusDTO
    ): TicketDTO {
        updateTicketStatusDTO.newState = State.IN_PROGRESS.toString();
        val ticket = ticketService.getTicket(id) ?: throw TicketNotFoundException("Ticket with id: $id not Found")
        return ticketService.assignExpert(ticket.toDTO(), expertId, updateTicketStatusDTO.requester_email).toDTO()
    }

    @PutMapping("/api/ticket/{id}/stop")
    fun stopTicket(@PathVariable("id") id: Long, @Valid @RequestBody updateTicketStatusDTO: UpdateTicketStatusDTO) {
        updateTicketStatusDTO.newState = State.OPEN.toString();
        stateHistoryService.updateState(updateTicketStatusDTO);
    }

    @PutMapping("/api/ticket/{id}/close")
    fun closeTicket(@PathVariable("id") id: Long, @Valid @RequestBody updateTicketStatusDTO: UpdateTicketStatusDTO) {
        updateTicketStatusDTO.newState = State.CLOSED.toString();
        stateHistoryService.updateState(updateTicketStatusDTO);
    }

    @PutMapping("/api/ticket/{id}/reopen")
    fun reopenTicket(@PathVariable("id") id: Long, @Valid @RequestBody updateTicketStatusDTO: UpdateTicketStatusDTO) {
        updateTicketStatusDTO.newState = State.REOPENED.toString();
        stateHistoryService.updateState(updateTicketStatusDTO);
    }

    @PutMapping("/api/ticket/{id}/resolve")
    fun resolveTicket(@PathVariable("id") id: Long, @Valid @RequestBody updateTicketStatusDTO: UpdateTicketStatusDTO) {
        updateTicketStatusDTO.newState = State.RESOLVED.toString();
        stateHistoryService.updateState(updateTicketStatusDTO);
    }

    @PostMapping("/api/ticket/{id}/message")
    fun addMessageToTicket(@PathVariable("id") id: Long, @Valid @RequestBody messageDTO: MessageDTO) {
        messageService.sendMessage(messageDTO)
    }


    @GetMapping("/api/ticket/{id}/messages")
    fun getMessagesFromTicket(@PathVariable("id") id: Long): List<MessageDTO> {
        return messageService.getChatMessages(id).map { it.toDTO() }
    }

    @GetMapping("/api/ticket/{id}/history")
    fun getHistoryFromTicket(@PathVariable("id") id: Long): List<StateHistoryDTO> {
        val ticket = ticketService.getTicket(id) ?: throw TicketNotFoundException("Ticket with id: $id not Found")
        return stateHistoryService.getHistory(ticket.toDTO()).map { it.toDTO() }
    }
}