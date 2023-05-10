package it.polito.wa2.g34.server.ticketing.dto

import it.polito.wa2.g34.server.ticketing.entity.Ticket
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size


data class TicketDTO(
    var id: Long? = null,
    var priority: String?,
    var state: String,
    @field:Email
    var creator_email: String,
    @field:Email
    var expert_mail: String?,
    @field:Size(min=13,max=13)
    var product_ean: String,
    var sale_id: String,
)

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(
        id=this.id,
        priority = if(this.priority == null) null else this.priority!!.name,
        state = this.state.toString(),
        creator_email = this.creator.email,
        expert_mail = this.expert?.email,
        product_ean = this.product.ean,
        sale_id = this.sale.id,
    )
}