package it.polito.wa2.g34.server.profile

import it.polito.wa2.g34.server.ticketing.entity.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository: JpaRepository<Profile, String> {
    @Query("SELECT p FROM Profile p WHERE p.role <> 'CUSTOMER'")
    fun getWorkers(): List<Profile>

}