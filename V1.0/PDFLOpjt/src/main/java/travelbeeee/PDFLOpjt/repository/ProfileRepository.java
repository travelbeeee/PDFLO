package travelbeeee.PDFLOpjt.repository;

import org.apache.ibatis.annotations.Mapper;
import travelbeeee.PDFLOpjt.domain.Profile;

@Mapper
public interface ProfileRepository {
    int insert(Profile profile);
    int delete(int profileId);
    int deleteAll();
    Profile selectByUserId(int userId);
}
