package it.polito.wa2.g34.server.ticketing.controller

 import io.micrometer.observation.annotation.Observed
 import it.polito.wa2.g34.server.observability.LogInfo
 import it.polito.wa2.g34.server.profile.ProfileService
 import it.polito.wa2.g34.server.profile.Role
 import it.polito.wa2.g34.server.ticketing.dto.*
 import it.polito.wa2.g34.server.ticketing.entity.Priority
 import it.polito.wa2.g34.server.ticketing.entity.State
 import it.polito.wa2.g34.server.ticketing.entity.Ticket
 import it.polito.wa2.g34.server.ticketing.service.MessageService
 import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
 import it.polito.wa2.g34.server.ticketing.service.TicketService
 import jakarta.validation.Valid
 import jakarta.validation.constraints.Email
 import org.slf4j.LoggerFactory
 import org.springframework.beans.factory.annotation.Autowired
 import org.springframework.core.io.FileSystemResource
 import org.springframework.http.MediaType
 import org.springframework.messaging.simp.SimpMessagingTemplate
 import org.springframework.security.access.prepost.PreAuthorize
 import org.springframework.security.core.context.SecurityContextHolder
 import org.springframework.validation.annotation.Validated
 import org.springframework.web.bind.annotation.*
 import org.springframework.web.multipart.MultipartFile
 import java.nio.file.Files
 import java.nio.file.StandardCopyOption
 import java.security.Principal
 import java.util.*
 import kotlin.io.path.Path


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

    @Autowired
    private val messageSender: SimpMessagingTemplate? = null


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
        val profile = profileService.getProfile(name)!!
        val tickets = when(profile.role) {
            Role.EXPERT -> ticketService.getTicketByExpert(profile) ?: emptyList()
            Role.MANAGER -> ticketService.getAllTickets()
            Role.CUSTOMER -> ticketService.getTicketByEmail(profile)
        }
        return stateHistoryService.sortTicketsByMostRecentState(tickets.map { it.toDTO() })
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
    fun addMessageToTicket(@PathVariable("id") id: Long, @Valid @RequestPart messageDTO: MessageDTO, @RequestPart("document", required = false) file: MultipartFile?) {

        if (file != null){
            val dirPath = Path("messagesfiles/$id")
            val fileName = UUID.randomUUID().toString()+'_'+file.originalFilename
            val filePath = Path(dirPath.toString(),fileName)
            if(!Files.exists(dirPath)){
                Files.createDirectories(dirPath)
            }
            Files.copy(file.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
            messageDTO.attachment = fileName
        }
        messageDTO.ticket_id = id
        messageService.sendMessage(messageDTO)
        messageSender!!.convertAndSend("/chat/$id", messageDTO)
    }

    @GetMapping("/api/ticket/{id}/message/{filename}",produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getMessageFile(@PathVariable("id") id: Long, @PathVariable("filename") filename: String) : FileSystemResource {
            val dirPath = Path("messagesfiles/$id")
            val fileName = filename
            val filePath = Path(dirPath.toString(),fileName)
            if(!Files.exists(filePath)){

                throw Exception();
            }
            return FileSystemResource(filePath)
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