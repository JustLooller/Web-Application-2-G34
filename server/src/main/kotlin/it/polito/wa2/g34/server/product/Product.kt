package it.polito.wa2.g34.server.product

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Entity
@Table(name = "product")
class Product (
    @Id
    var ean: String="",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand")
    var brand: Brand,
    var model: String = "",
    var description: String = "",
    var image: String = ""
)

@Entity
class Brand(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int=0,
    var name: String
)

@Repository
interface BrandRepository: JpaRepository<Brand, String> {

}