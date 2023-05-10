package it.polito.wa2.g34.server.product

interface ProductService {
    fun getAll(): List<ProductDTO>
    fun getProduct(ean: String): Product?
}