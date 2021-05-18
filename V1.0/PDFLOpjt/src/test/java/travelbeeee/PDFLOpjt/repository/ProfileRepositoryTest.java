package travelbeeee.PDFLOpjt.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLOpjt.domain.Profile;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProfileRepositoryTest {
    @Autowired
    ProfileRepository profileRepository;

    @AfterEach
    void afterEach(){
        profileRepository.deleteAll();
    }

    Profile getProfile(){
        Profile profile = new Profile();
        profile.setUserId(1);
        profile.setOriginFileName("origin");
        profile.setSaltedFileName("salted");
        profile.setLocation("location");
        return profile;
    }

    @Test
    public void 프로필Insert테스트() throws Exception{
        //given
        Profile profile = getProfile();

        //when
        int res = profileRepository.insert(profile);

        //then
        assertThat(res).isEqualTo(1);
    }

    @Test
    public void 프로필delete테스트() throws Exception{
        //given
        Profile profile = getProfile();
        //when
        profileRepository.insert(profile);
        int res = profileRepository.delete(profile.getProfileId());

        //then
        assertThat(res).isEqualTo(1);
    }
}