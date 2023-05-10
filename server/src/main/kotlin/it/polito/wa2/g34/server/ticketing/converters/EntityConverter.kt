package it.polito.wa2.g34.server.ticketing.converters

import it.polito.wa2.g34.server.sales.Sale
import it.polito.wa2.g34.server.sales.SaleDTO
import it.polito.wa2.g34.server.ticketing.dto.*
import it.polito.wa2.g34.server.ticketing.entity.*

interface EntityConverter {

    fun ticketDTOtoEntity(ticketDTO: TicketDTO): Ticket;

    fun messageDTOtoEntity(messageDTO: MessageDTO): Message;

    fun stateHistoryDTOtoEntity(stateHistoryDTO: StateHistoryDTO): StateHistory;

    fun saleDTOtoEntity(saleDTO: SaleDTO): Sale;
}