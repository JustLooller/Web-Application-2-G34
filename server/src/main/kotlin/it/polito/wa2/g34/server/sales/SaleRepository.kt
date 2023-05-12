package it.polito.wa2.g34.server.sales

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SaleRepository: JpaRepository<Sale, String> {
}