package it.polito.wa2.g34.server.ticketing.dto

import it.polito.wa2.g34.server.product.ProductDTO
import it.polito.wa2.g34.server.product.toDTO
import it.polito.wa2.g34.server.product.toEntity
import it.polito.wa2.g34.server.profile.ProfileDTO
import it.polito.wa2.g34.server.profile.toDTO
import it.polito.wa2.g34.server.profile.toEntity
import it.polito.wa2.g34.server.sales.SaleService
import it.polito.wa2.g34.server.sales.toEntity
import it.polito.wa2.g34.server.ticketing.entity.Priority
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.entity.Ticket
import it.polito.wa2.g34.server.ticketing.service.MessageService
import it.polito.wa2.g34.server.ticketing.service.StateHistoryService
import org.springframework.beans.factory.annotation.Autowired


data class TicketDTO(
    var id: Long? = null,
    var priority: String,
    var state: String,
    var creator: ProfileDTO,
    var expert: ProfileDTO?,
    var product: ProductDTO,
    var sale_id: String,
)


fun TicketDTO.toEntity(sale: SaleService) : Ticket {
    return Ticket(
        id=this.id,
        priority= Priority.valueOf(this.priority),
        state=State.valueOf(this.state),
        creator= this.creator.toEntity(),
        expert=this.expert?.toEntity(),
        product=this.product.toEntity(),
        sale= sale.getSaleByTicket(this).toEntity(),
    )
}





fun Ticket.toDTO() : TicketDTO {
    return TicketDTO(
        id = this.id,
        priority = this.priority.toString(),
        state = this.state.toString(),
        creator = this.creator.toDTO(),
        expert = this.expert?.toDTO(),
        product = this.product.toDTO(),
        sale_id = this.sale.id,
    )
}