package it.polito.wa2.g34.server.profile

import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository
): ProfileService {
    override fun getProfile(email: String) : Profile? {
        return profileRepository.findById(email).orElse(null);
    }

    override fun postProfile(newProfile: ProfileDTO) : Profile? {
        return profileRepository.save(newProfile.toEntity());
    }

}