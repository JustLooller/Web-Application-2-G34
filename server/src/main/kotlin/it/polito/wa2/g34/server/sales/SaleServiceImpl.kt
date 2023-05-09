package it.polito.wa2.g34.server.sales

import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrDefault
@Service
class SaleServiceImpl(
    private val saleRepository: SaleRepository
) : SaleService{


    override fun getSaleByTicket(ticket: TicketDTO): SaleDTO {
        var found = saleRepository.findById(ticket.sale_id);
        if (found.isPresent) {
            return found.get().toDTO()
        } else {
//            throw SaleNotFoundException()
            TODO("throw SaleNotFoundException()")
        }
    }
}