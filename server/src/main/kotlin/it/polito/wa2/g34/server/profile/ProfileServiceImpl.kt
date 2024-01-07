package it.polito.wa2.g34.server.profile

import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository
): ProfileService {
    override fun getProfile(email: String) : Profile? {
        return try {
            profileRepository.findById(email).get()
        } catch (e: NoSuchElementException) {
            null
        }
    }

    override fun postProfile(newProfile: ProfileDTO) : Profile? {
        return profileRepository.save(newProfile.toEntity());
    }

}