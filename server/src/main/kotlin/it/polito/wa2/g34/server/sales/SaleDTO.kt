package it.polito.wa2.g34.server.sales

import it.polito.wa2.g34.server.product.toEntity
import it.polito.wa2.g34.server.profile.toDTO
import it.polito.wa2.g34.server.profile.toEntity
import java.time.LocalDateTime

data class SaleDTO(
    var id : String,
    var product_ean : String,
    var buyer_mail: String?,
    var warranty_start: LocalDateTime,
    var warranty_end: LocalDateTime,
)

fun Sale.toDTO(): SaleDTO{
    return SaleDTO(
        id=this.id,
        product_ean=this.product.ean,
        buyer_mail= this.buyer?.email,
        warranty_start= warranty_start,
        warranty_end=this.warranty_end,
    )
}

