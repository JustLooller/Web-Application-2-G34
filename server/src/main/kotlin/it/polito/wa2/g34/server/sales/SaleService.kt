package it.polito.wa2.g34.server.sales

import it.polito.wa2.g34.server.ticketing.dto.TicketDTO

interface SaleService {

    fun getSale(sale_id: String): Sale
    fun getSaleByTicket(ticket: TicketDTO): Sale
}