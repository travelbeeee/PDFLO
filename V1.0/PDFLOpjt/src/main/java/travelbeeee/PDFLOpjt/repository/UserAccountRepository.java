package travelbeeee.PDFLOpjt.repository;

import org.apache.ibatis.annotations.Mapper;
import travelbeeee.PDFLOpjt.domain.UserAccount;

@Mapper
public interface UserAccountRepository {
    int insert(UserAccount userAccount);
    int update(UserAccount userAccount);
    int delete(int userId);
    int deleteAll();
    UserAccount selectByUser(int userId);
}

