package it.polito.wa2.g34.server.sales

import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrDefault
@Service
class SaleServiceImpl(
    private val saleRepository: SaleRepository
) : SaleService{


    override fun getSale(sale_id: String): Sale {
        var found = saleRepository.findById(sale_id);
        if (found.isPresent) {
            return found.get()
        } else {
            throw SaleNotFoundException(sale_id)
        }
    }
    override fun getSaleByTicket(ticket: TicketDTO): Sale {
        return getSale(ticket.sale_id);
    }
}