package it.polito.wa2.g34.server.profile

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository: JpaRepository<Profile, String> {

}