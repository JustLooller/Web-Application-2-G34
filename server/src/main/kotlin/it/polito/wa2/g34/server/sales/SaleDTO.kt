package it.polito.wa2.g34.server.sales

import it.polito.wa2.g34.server.product.ProductDTO
import it.polito.wa2.g34.server.product.toDTO
import it.polito.wa2.g34.server.product.toEntity
import it.polito.wa2.g34.server.profile.ProfileDTO
import it.polito.wa2.g34.server.profile.toDTO
import it.polito.wa2.g34.server.profile.toEntity
import java.time.LocalDateTime

data class SaleDTO(
    var id : String,
    var product : ProductDTO,
    var buyer: ProfileDTO,
    var warranty_start: LocalDateTime,
    var warranty_end: LocalDateTime,
)

fun Sale.toDTO(): SaleDTO{
    return SaleDTO(
        id=this.id,
        product=this.product.toDTO(),
        buyer=this.buyer.toDTO(),
        warranty_start= warranty_start,
        warranty_end=this.warranty_end,
    )
}

fun SaleDTO.toEntity() : Sale{
    return Sale(
        id=this.id,
        product=this.product.toEntity(),
        buyer=this.buyer.toEntity(),
        warranty_start=this.warranty_start,
        warranty_end=this.warranty_end
    )
}
