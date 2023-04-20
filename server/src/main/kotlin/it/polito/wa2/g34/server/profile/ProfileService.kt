package it.polito.wa2.g34.server.profile

interface ProfileService {
    fun getProfile(email: String): ProfileDTO?

    fun postProfile(newProfile: ProfileDTO) : ProfileDTO?
}