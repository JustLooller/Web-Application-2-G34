package it.polito.wa2.g34.server.product

import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
): ProductService {

    override fun getAll() : List<ProductDTO>{
        return productRepository.findAll().map { it.toDTO() };
    }
    override fun getProduct(ean: String) : Product? {
        return productRepository.findById(ean).orElse(null);
    }
}