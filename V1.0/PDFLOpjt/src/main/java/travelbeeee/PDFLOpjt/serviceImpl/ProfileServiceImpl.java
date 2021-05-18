package travelbeeee.PDFLOpjt.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLOpjt.domain.Profile;
import travelbeeee.PDFLOpjt.repository.ProfileRepository;
import travelbeeee.PDFLOpjt.service.ProfileService;

@Service @RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    @Override
    public Profile selectByUserId(int userId) {
        Profile profile = profileRepository.selectByUserId(userId);

        return profile;
    }
}
