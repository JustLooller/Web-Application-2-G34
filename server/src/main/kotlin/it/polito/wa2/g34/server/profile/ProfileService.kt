package it.polito.wa2.g34.server.profile

import it.polito.wa2.g34.server.ticketing.entity.Ticket

interface ProfileService {
    fun getProfile(email: String): Profile?

    fun getWorkers():  List<Profile>

    fun postProfile(newProfile: ProfileDTO) : Profile?
}