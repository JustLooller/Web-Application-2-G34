package it.polito.wa2.g34.server.sales

import it.polito.wa2.g34.server.profile.Profile
import it.polito.wa2.g34.server.profile.ProfileDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO

interface SaleService {

    fun getSale(sale_id: String): Sale
    fun getSaleByTicket(ticket: TicketDTO): Sale
    fun getSalesByProfile(profile: Profile): List<Sale>
}