package it.polito.wa2.g34.server.product

import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class ProductController(
    private val productService: ProductService
) {
    @GetMapping("/products/")
    fun getAll(): List<ProductDTO> {
        return productService.getAll()
    }

    @GetMapping("/products/{ean}")
    fun getProduct(@PathVariable @Size(min=13,max=13) ean: String ): ProductDTO? {
        return productService.getProduct(ean) ?: throw ProductNotFoundException("Product with ean: $ean not Found")
    }
}