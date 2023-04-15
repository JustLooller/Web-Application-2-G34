package it.polito.wa2.g34.server

import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
): ProductService {

    override fun getAll() : List<ProductDTO>{
        return productRepository.findAll().map { it.toDTO() };
    }
    override fun getProduct(ean: String) : ProductDTO? {
        return productRepository.findById(ean).map { it.toDTO() }.orElse(null);
    }
}