package it.polito.wa2.g34.server.profile

import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository
): ProfileService {
    override fun getProfile(email: String) : ProfileDTO? {
        return profileRepository.findById(email).map { it.toDTO() }.orElse(null);
    }

    override fun postProfile(newProfile: Profile) : ProfileDTO? {
        return profileRepository.save(newProfile).toDTO();
    }

}