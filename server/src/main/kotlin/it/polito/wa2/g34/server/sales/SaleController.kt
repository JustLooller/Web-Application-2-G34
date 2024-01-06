package it.polito.wa2.g34.server.sales

import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g34.server.observability.LogInfo
import it.polito.wa2.g34.server.profile.ProfileService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000/", "http://localhost:5500/"])
@Validated
@Observed
@LogInfo
class SaleController(
    private val saleService: SaleService,
    private val profileService: ProfileService,
    private val saleRepository: SaleRepository
) {

    @PostMapping("/api/sale/{id}")
    fun associateSale(@PathVariable("id") id: String): Sale {
        val authentication = SecurityContextHolder.getContext().authentication
        var sale = saleService.getSale(id)
        if(sale.buyer != null)
            throw SaleAlreadyAssociatedException(sale.id)
        sale.buyer = profileService.getProfile(authentication.name)
        saleRepository.save(sale)
        return sale
    }

    @GetMapping("/api/sale")
    fun getSales(): List<Sale> {
        val authentication = SecurityContextHolder.getContext().authentication
        val profile = profileService.getProfile(authentication.name)!!
        return saleService.getSalesByProfile(profile)
    }
}