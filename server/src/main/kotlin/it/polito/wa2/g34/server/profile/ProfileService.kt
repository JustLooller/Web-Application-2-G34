package it.polito.wa2.g34.server.profile

interface ProfileService {
    fun getProfile(email: String): Profile?

    fun postProfile(newProfile: ProfileDTO) : Profile?
}