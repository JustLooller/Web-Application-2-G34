package it.polito.wa2.g34.server

data class ProductDTO(
    var ean: String,
    var brand: Int,
    var model: String,
    var description: String,
    var image: String
)

fun Product.toDTO(): ProductDTO{
    return ProductDTO(
        ean = this.ean,
        brand = this.brand,
        model = this.model,
        description = this.description,
        image = this.image
    )
}