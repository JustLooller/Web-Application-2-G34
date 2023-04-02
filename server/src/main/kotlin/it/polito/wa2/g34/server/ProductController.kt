package it.polito.wa2.g34.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productService: ProductService
) {
    @GetMapping("/products/")
    fun getAll(): List<ProductDTO> {
        return productService.getAll()
    }
    @GetMapping("/products/{ean}")
    fun getProduct(@PathVariable ean: String): ProductDTO? {
        return productService.getProduct(ean)
    }
}