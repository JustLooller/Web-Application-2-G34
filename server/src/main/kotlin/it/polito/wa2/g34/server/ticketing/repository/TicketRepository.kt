package it.polito.wa2.g34.server.ticketing.repository

import it.polito.wa2.g34.server.profile.Profile
import it.polito.wa2.g34.server.sales.Sale
import it.polito.wa2.g34.server.ticketing.entity.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository: JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.creator = ?1")
    fun getByEmail(profile: Profile): List<Ticket>

    @Query("SELECT t FROM Ticket t WHERE t.sale = ?1")
    fun findBySaleId(saleId: Long): List<Ticket>

    @Query("SELECT t FROM Ticket t WHERE t.expert = ?1")
    fun findByExpertId(expertId: Profile): List<Ticket>?;
}