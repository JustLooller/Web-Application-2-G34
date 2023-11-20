package it.polito.wa2.g34.server.sales

import it.polito.wa2.g34.server.profile.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SaleRepository: JpaRepository<Sale, String> {
    @Query("SELECT s FROM Sale s WHERE s.buyer = ?1")
    fun findAllByBuyer(profile: Profile): List<Sale>
}