package it.polito.wa2.g34.server.ticketing.converters

import it.polito.wa2.g34.server.product.ProductService
import it.polito.wa2.g34.server.profile.ProfileService
import it.polito.wa2.g34.server.sales.Sale
import it.polito.wa2.g34.server.sales.SaleDTO
import it.polito.wa2.g34.server.sales.SaleService
import it.polito.wa2.g34.server.ticketing.advice.TicketBadRequestException
import it.polito.wa2.g34.server.ticketing.dto.MessageDTO
import it.polito.wa2.g34.server.ticketing.dto.StateHistoryDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.entity.*
import it.polito.wa2.g34.server.ticketing.service.TicketService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class EntityConverterImpl(
    @Lazy private val saleService: SaleService,
    @Lazy private val profileService: ProfileService,
    @Lazy private val productService: ProductService,
    @Lazy private val ticketService: TicketService
) : EntityConverter {
    override fun ticketDTOtoEntity(ticketDTO: TicketDTO): Ticket {
        return Ticket(
            id = ticketDTO.id,
            priority = if (ticketDTO.priority == null) null else Priority.valueOf(ticketDTO.priority!!),
            state = State.valueOf(ticketDTO.state),
            creator = profileService.getProfile(ticketDTO.creator_email)!!, // TODO: throw error in service if no profile found
            expert = ticketDTO.expert_mail?.let { profileService.getProfile(it) },
            product = saleService.getSale(ticketDTO.sale_id).product,
            sale = saleService.getSale(ticketDTO.sale_id),
        )
    }

    override fun messageDTOtoEntity(messageDTO: MessageDTO): Message {
        try {
            return Message(
                id = messageDTO.id,
                text = messageDTO.text,
                attachment = messageDTO.attachment,
                user = profileService.getProfile(messageDTO.user_mail)!!,
                ticket = ticketService.getTicket(messageDTO.ticket_id)!!,
            )
        } catch (e: Exception) {
            throw TicketBadRequestException("Invalid message")
        }
    }

    override fun stateHistoryDTOtoEntity(stateHistoryDTO: StateHistoryDTO): StateHistory {
        return StateHistory(
            id = stateHistoryDTO.id,
            timestamp = stateHistoryDTO.timestamp,
            status = State.valueOf(stateHistoryDTO.status),
            user = profileService.getProfile(stateHistoryDTO.user_mail)!!,
            ticket = ticketService.getTicket(stateHistoryDTO.ticket_id)!!,
        )
    }

    override fun saleDTOtoEntity(saleDTO: SaleDTO): Sale {
        return Sale(
            id = saleDTO.id,
            product = productService.getProduct(saleDTO.product_ean)!!,
            buyer = profileService.getProfile(saleDTO.buyer_mail)!!,
            warranty_start = saleDTO.warranty_start,
            warranty_end = saleDTO.warranty_end,
        )
    }
}