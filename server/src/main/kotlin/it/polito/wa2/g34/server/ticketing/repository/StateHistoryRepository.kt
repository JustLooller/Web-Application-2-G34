package it.polito.wa2.g34.server.ticketing.repository

import it.polito.wa2.g34.server.ticketing.entity.StateHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StateHistoryRepository : JpaRepository<StateHistory, Long> {
    fun findByTicketId(ticketId: Long): List<StateHistory>;


}