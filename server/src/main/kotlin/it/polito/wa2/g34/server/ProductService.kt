package it.polito.wa2.g34.server

interface ProductService {
    fun getAll(): List<ProductDTO>
    fun getProduct(ean: String): ProductDTO?
}